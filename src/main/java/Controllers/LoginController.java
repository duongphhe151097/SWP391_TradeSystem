package Controllers;

import DataAccess.CaptchaRepository;
import DataAccess.UserRepository;
import Models.CaptchaEntity;
import Models.UserEntity;
import Services.CaptchaService;
import Utils.Annotations.Authentication;
import Utils.Constants.UserConstant;
import Utils.Generators.StringGenerator;
import Utils.Validation.StringValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "LoginController", urlPatterns = "/login")
@Authentication(isPublic = true)
public class LoginController extends BaseController {
    private CaptchaService captchaService;

    @Override
    public void init() throws ServletException {
        this.captchaService = new CaptchaService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String captcha = req.getParameter("captcha");
        String hiddenCaptchaId = req.getParameter("");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/login.jsp");

        try {
            boolean isUsername = StringValidator.isValidUsername(username);
            boolean isPassword = StringValidator.isValidPassword(password);
            boolean isValidCaptcha = captcha != null && !captcha.isBlank();
            boolean isValidHiddenCaptcha = hiddenCaptchaId != null && !hiddenCaptchaId.isBlank();

            if (!isUsername) {
                req.setAttribute("USERNAME_ERROR", "Tên tài khoản không hợp lệ");
            }

            if (!isPassword) {
                req.setAttribute("USERNAME_ERROR", "Mật khẩu không hợp lệ");
            }

            if (!isValidCaptcha) {
                req.setAttribute("CAPTCHA_ERROR", "Captcha ko đc để trống!");
            }

            if (!isValidHiddenCaptcha) {
                req.setAttribute("CAPTCHA_ERROR", "Captcha ko không hợp lệ");
            }

            boolean isAllValid = StringValidator
                    .isTrue(isUsername,
                            isPassword,
                            isValidCaptcha,
                            isValidHiddenCaptcha);
            if (!isAllValid) {
                dispatcher.forward(req, resp);
                return;
            }

            //Check captcha`
            if (captchaService.isValidCaptcha(captcha, hiddenCaptchaId)) {
                req.setAttribute("VAR_USERNAME", username);
                req.setAttribute("VAR_EMAIL", password);
                req.setAttribute("CAPTCHA_ERROR", "Captcha bạn nhập không đúng!");
                dispatcher.forward(req, resp);
                return;
            }

            //User logic
            UserRepository repository = new UserRepository();

            Optional<UserEntity> existUser = repository.getUserByUsername(username);
            if (existUser.isEmpty() || !StringGenerator.verifyPassword(password, existUser.get().getPassword(), existUser.get().getSalt())) {
                req.setAttribute("FAILED_MESSAGE", "Sai tài khoản hoặc mật khẩu!");
                dispatcher.forward(req, resp);
                return;
            }

            if (existUser.get().getStatus() == UserConstant.PENDING) {
                req.setAttribute("FAILED_MESSAGE", "Tài khoản của bạn chưa được kích hoạt!");
                dispatcher.forward(req, resp);
                return;
            }

            if (existUser.get().getStatus() == UserConstant.LOCKED) {
                req.setAttribute("FAILED_MESSAGE", "Tài khoản của bạn đã bị khóa!");
                dispatcher.forward(req, resp);
                return;
            }

            resp.sendRedirect("home");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("FAILED_MESSAGE", "Có lỗi xảy ra khi dăngd nhập");
            dispatcher.forward(req, resp);
        }
    }
}

