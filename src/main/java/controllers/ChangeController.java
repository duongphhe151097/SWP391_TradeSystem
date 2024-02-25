package controllers;

import dataAccess.UserRepository;
import models.UserEntity;
import services.CaptchaService;
import utils.annotations.Authorization;
import utils.constants.UserConstant;
import utils.generators.StringGenerator;
import utils.validation.StringValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;





@WebServlet(name = "ChangeController", urlPatterns = "/change")
@Authorization(role = "", isPublic = false)
public class ChangeController extends BaseController {
    private CaptchaService captchaService;

    public void init() throws ServletException {
        this.captchaService = new CaptchaService();
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/change.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);

        if (userId == null) {
            // Người dùng chưa đăng nhập, có thể chuyển hướng hoặc xử lý tùy vào yêu cầu
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        String reNewPassword = req.getParameter("reNewPassword");
//        String captcha = req.getParameter("captcha");
//        String hiddenCaptchaId = req.getParameter("hidden_id");
//        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/change.jsp");

        try {
            UserRepository userRepository = new UserRepository();
            Optional<UserEntity> userOptional = userRepository.getUserById(userId);

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();
                String storedPassword = user.getPassword();
                String storedSalt = user.getSalt();

                // Giải mã mật khẩu cũ
                String decryptedOldPassword = StringGenerator.hashingPassword(oldPassword, storedSalt);

                // Kiểm tra xem mật khẩu cũ có khớp không
                if (storedPassword.equals(decryptedOldPassword)) {
                    // Kiểm tra tính hợp lệ của mật khẩu mới và xác nhận mật khẩu mới
                    boolean isValidPassword = StringValidator.isValidPassword(newPassword);
                    boolean isRePasswordMatch = newPassword.equals(reNewPassword);
//                    boolean isValidCaptcha = captcha != null && !captcha.isBlank();
//                    boolean isValidHiddenCaptcha = hiddenCaptchaId != null && !hiddenCaptchaId.isBlank();

                    if (isValidPassword && isRePasswordMatch) {
                        // Cập nhật mật khẩu mới
                        String newSalt = StringGenerator.generateRandomString(50);
                        String hashedNewPassword = StringGenerator.hashingPassword(newPassword, newSalt);

                        userRepository.updateUserPassword(userId, hashedNewPassword, newSalt);

                        req.setAttribute("resultMessage", "Thay đổi mật khẩu thành công!");
                    }
//                        if (captchaService.isValidCaptcha(captcha, hiddenCaptchaId)) {
//
//                            req.setAttribute("CAPTCHA_ERROR", "Captcha bạn nhập không đúng!");
//                            dispatcher.forward(req, resp);
//                            return;

//                    }
                else {
                        req.setAttribute("resultMessage", "Mật khẩu mới không hợp lệ hoặc không khớp!");
                    }
                } else {
                    req.setAttribute("resultMessage", "Mật khẩu cũ không đúng!");
                }
            } else {
                req.setAttribute("resultMessage", "Người dùng không tồn tại!");
            }
        } catch (Exception e) {
            req.setAttribute("resultMessage", "ID người dùng không hợp lệ!");
        }

        // Forward đến trang change.jsp để hiển thị thông báo kết quả
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/change.jsp");
        dispatcher.forward(req, resp);
    }
}