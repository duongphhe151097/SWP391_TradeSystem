package controllers;

import dataAccess.TokenActivationRepository;
import dataAccess.UserRepository;
import jakarta.servlet.RequestDispatcher;
import models.TokenActivationEntity;
import utils.annotations.Authorization;
import utils.constants.ActivationType;
import utils.generators.StringGenerator;
import utils.validation.StringValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "ResetPasswordController", urlPatterns = "/resetpassword")
@Authorization(role = "", isPublic = true)
public class ResetPasswordController extends BaseController {

    private TokenActivationRepository tokenRepository;
    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        this.tokenRepository = new TokenActivationRepository();
        this.userRepository = new UserRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/resetpassword.jsp");

        String resetToken = req.getParameter("t");

        if (StringValidator.isNullOrBlank(resetToken)) {
            req.setAttribute("FAILED_MESSAGE", "Token không hợp lệ!");
            dispatcher.forward(req, resp);
            return;
        }

        // Kiểm tra tính hợp lệ của token ở đây
        Optional<TokenActivationEntity> tokenOptional = tokenRepository.getTokenByToken(resetToken);

        if (tokenOptional.isEmpty() || tokenOptional.get().isUsed() || tokenOptional.get().getType() != ActivationType.RESET_PASSWORD) {
            req.setAttribute("FAILED_MESSAGE", "Token không hợp lệ!");
            dispatcher.forward(req, resp);
            return;
        }

        req.setAttribute("resetToken", resetToken);
        dispatcher.forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/resetpassword.jsp");
        String resetToken = req.getParameter("resetToken");
        String newPassword = req.getParameter("newpassword");
        String reNewPassword = req.getParameter("renewpassword");
        try {
            // Lấy thông tin người dùng dựa trên resetToken
            Optional<TokenActivationEntity> tokenOptional = new TokenActivationRepository().getTokenByToken(resetToken);

            if (tokenOptional.isEmpty() || tokenOptional.get().isUsed() || tokenOptional.get().getType() != ActivationType.RESET_PASSWORD) {
                req.setAttribute("FAILED_MESSAGE", "Token không hợp lệ!");
                dispatcher.forward(req, resp);
                return;
            }

            TokenActivationEntity token = tokenOptional.get();
            UUID userId = token.getUserId();

            // Kiểm tra tính hợp lệ của mật khẩu mới và xác nhận mật khẩu mới
            boolean isValidPassword = StringValidator.isValidPassword(newPassword);
            boolean isRePasswordMatch = newPassword.equals(reNewPassword);

            if (!isValidPassword) {
                req.setAttribute("NEWPASSWORD_ERROR", "Mật khẩu mới phải chứa ít nhất 8 kí tự!");
                dispatcher.forward(req, resp);
                return;
            }

            if (!isRePasswordMatch) {
                req.setAttribute("RENEWPASSWORD_ERROR", "Mật khẩu mới nhập lại không khớp!");
                dispatcher.forward(req, resp);
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
            dispatcher.forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("FAILED_MESSAGE", "Có lỗi xảy ra khi thay đổi mật khẩu");
            dispatcher.forward(req, resp);
        }
    }
}