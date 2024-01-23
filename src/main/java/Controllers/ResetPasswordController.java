package Controllers;

import DataAccess.TokenActivationRepository;
import DataAccess.UserRepository;
import Models.TokenActivationEntity;
import Utils.Generators.StringGenerator;
import Utils.Validation.StringValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
@WebServlet(name = "ResetPasswordController", urlPatterns = "/resetpassword")
public class ResetPasswordController extends BaseController{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resetToken = req.getParameter("t");

        if (resetToken == null || resetToken.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/error.jsp");
        } else {
            // Kiểm tra tính hợp lệ của token ở đây
            TokenActivationRepository tokenRepository = new TokenActivationRepository();
            Optional<TokenActivationEntity> tokenOptional = tokenRepository.getTokenByToken(resetToken);

            if (tokenOptional.isPresent() && !tokenOptional.get().isUsed()) {
                req.setAttribute("resetToken", resetToken);
                req.getRequestDispatcher("/pages/resetpassword.jsp").forward(req, resp);
            } else {
                // Token không hợp lệ, có thể chuyển hướng hoặc hiển thị thông báo lỗi
                resp.sendRedirect(req.getContextPath() + "/error.jsp");
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resetToken = req.getParameter("resetToken");

        try {
            UserRepository userRepository = new UserRepository();

            // Lấy thông tin người dùng dựa trên resetToken
            Optional<TokenActivationEntity> tokenOptional = new TokenActivationRepository().getTokenByToken(resetToken);

            if (tokenOptional.isPresent() && !tokenOptional.get().isUsed()) {
                TokenActivationEntity token = tokenOptional.get();
                UUID userId = token.getUserId();

                String newPassword = req.getParameter("newpassword");
                String reNewPassword = req.getParameter("renewpassword");

                // Kiểm tra tính hợp lệ của mật khẩu mới và xác nhận mật khẩu mới
                boolean isValidPassword = StringValidator.isValidPassword(newPassword);
                boolean isRePasswordMatch = newPassword.equals(reNewPassword);

                if (!isValidPassword) {
                    req.setAttribute("NEWPASSWORD_ERROR", "Mật khẩu mới phải chứa ít nhất 8 kí tự!");
                    req.getRequestDispatcher("/pages/resetpassword.jsp").forward(req, resp);
                    return;
                }

                if (!isRePasswordMatch) {
                    req.setAttribute("RENEWPASSWORD_ERROR", "Mật khẩu mới nhập lại không khớp!");
                    req.getRequestDispatcher("/pages/resetpassword.jsp").forward(req, resp);
                    return;
                }

                // Cập nhật mật khẩu mới
                String salt = StringGenerator.generateRandomString(50);
                String hashedPassword = StringGenerator.hashingPassword(newPassword, salt);


                userRepository.updateUserPassword(userId, hashedPassword, salt);

                // Đánh dấu token đã sử dụng
                token.setUsed(true);
                new TokenActivationRepository().updateToken(token);

                req.setAttribute("SUCCESS_MESSAGE", "Mật khẩu đã được thay đổi thành công!");
                req.getRequestDispatcher("/pages/resetpassword.jsp").forward(req, resp);
            } else {
                    // Token không hợp lệ, có thể chuyển hướng hoặc hiển thị thông báo lỗi
                    resp.sendRedirect(req.getContextPath() + "/error.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("FAILED_MESSAGE", "Có lỗi xảy ra khi thay đổi mật khẩu");
            req.getRequestDispatcher("/pages/resetpassword.jsp").forward(req, resp);
        }
    }
}