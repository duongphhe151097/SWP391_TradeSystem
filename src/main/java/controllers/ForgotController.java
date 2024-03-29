package controllers;

import dataAccess.TokenActivationRepository;
import dataAccess.UserRepository;
import models.TokenActivationEntity;
import models.UserEntity;
import services.CaptchaService;
import services.SendMailService;
import utils.annotations.Authorization;
import utils.constants.ActivationType;
import utils.generators.StringGenerator;
import utils.validation.StringValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "ForgotController", urlPatterns = "/forgot")
@Authorization(role = "", isPublic = true)
public class ForgotController extends BaseController {
    private CaptchaService captchaService;
    private UserRepository userRepository;
    private TokenActivationRepository tokenRepository;

    public void init() throws ServletException {
        this.captchaService = new CaptchaService();
        this.userRepository = new UserRepository();
        this.tokenRepository = new TokenActivationRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/forgot.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String captcha = req.getParameter("captcha");
        String hiddenCaptchaId = req.getParameter("hidden_id");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/forgot.jsp");

        try {
            boolean isValidEmail = StringValidator.isValidEmail(email);
            boolean isValidCaptcha = captcha != null && !captcha.isBlank();
            boolean isValidHiddenCaptcha = hiddenCaptchaId != null && !hiddenCaptchaId.isBlank();

            if (!isValidEmail) {
                req.setAttribute("EMAIL_ERROR", "Email không hợp lệ!");
                dispatcher.forward(req, resp);
                return;
            }
            if (!isValidCaptcha) {
                req.setAttribute("CAPTCHA_ERROR", "Captcha không được để trống!");
            }

            if (!isValidHiddenCaptcha) {
                req.setAttribute("CAPTCHA_ERROR", "Captcha không hợp lệ");
            }

            boolean isAllValid = StringValidator.isTrue(isValidEmail, isValidCaptcha, isValidHiddenCaptcha);
            if (!isAllValid) {
                dispatcher.forward(req, resp);
                return;
            }

            if (!captchaService.isValidCaptcha(captcha, hiddenCaptchaId)) {
                req.setAttribute("VAR_EMAIL", "email");
                req.setAttribute("CAPTCHA_ERROR", "Captcha bạn nhập không đúng!");
                dispatcher.forward(req, resp);
                return;
            }

            Optional<UserEntity> userOptional = userRepository.getUserByEmail(email);

            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                // Tạo reset token
                String resetToken = StringGenerator.generateRandomString(50);

                // Lưu reset token vào DB
                TokenActivationEntity tokenEntity = TokenActivationEntity.builder()
                        .id(UUID.randomUUID())
                        .token(resetToken)
                        .userId(user.getId())
                        .type(ActivationType.RESET_PASSWORD)
                        .isUsed(false)
                        .createAt(LocalDateTime.now())
                        .expriedAt(LocalDateTime.now().plusDays(1))
                        .build();

                tokenRepository.addToken(tokenEntity);

                String resetLink = getBaseURL(req) + "/resetpassword?t=" + resetToken;
                SendMailService.sendMail(email, "[Trade System] - Quên mật khẩu", "Nhấn vào liên kết sau để đặt lại mật khẩu: " + resetLink);

                req.setAttribute("SUCCESS_MESSAGE", "Đã gửi hướng dẫn đặt lại mật khẩu vào email của bạn!");
                dispatcher.forward(req, resp);
            } else {
                req.setAttribute("FAILED_MESSAGE", "Không tìm thấy người dùng với địa chỉ email này!");
                dispatcher.forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("FAILED_MESSAGE", "Có lỗi xảy ra khi thực hiện quên mật khẩu");
            dispatcher.forward(req, resp);
        }

    }
}