package Controllers;

import DataAccess.UserRepository;
import Models.UserEntity;
import Utils.Generators.StringGenerator;
import Utils.Validation.StringValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ChangeController", urlPatterns = "/change")
public class ChangeController extends BaseController{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newPassword = req.getParameter("newpassword");
        String reNewPassword = req.getParameter("renewpassword");
        String userEnteredCaptcha = req.getParameter("captcha");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/change.jsp");

        try {
            // Kiểm tra captcha
            String generatedCaptcha = (String) req.getSession().getAttribute("captcha");
            if (userEnteredCaptcha == null || !userEnteredCaptcha.equals(generatedCaptcha)) {
                req.setAttribute("CAPTCHA_ERROR", "Captcha không đúng!");
                dispatcher.forward(req, resp);
                return;
            }

            boolean isValidPassword = StringValidator.isValidPassword(newPassword);
            boolean isRePasswordMatch = newPassword.equals(reNewPassword);

            if (!isValidPassword) {
                req.setAttribute("NEWPASSWORD_ERROR", "Password phải chứa ít nhất 8 kí tự!");
            }

            if (!isRePasswordMatch) {
                req.setAttribute("RENEWPASSWORD_ERROR", "Password nhập lại không khớp!");
            }

            boolean isAllValid = StringValidator.isTrue(isValidPassword, isRePasswordMatch);
            if (!isAllValid) {
                dispatcher.forward(req, resp);
                return;
            }

            UserRepository repository = new UserRepository();

            // Không cần kiểm tra resetToken, vì đã loại bỏ token từ quá trình này

            UserEntity user = new UserEntity();

            if (user == null) {
                req.setAttribute("FAILED_MESSAGE", "Người dùng không hợp lệ");
                dispatcher.forward(req, resp);
                return;
            }

            String salt = StringGenerator.generateRandomString(50);
            String hashedPassword = StringGenerator.hashingPassword(newPassword, salt);
            user.setPassword(hashedPassword);
            user.setSalt(salt);

            repository.updateUser(user);

            req.setAttribute("SUCCESS_MESSAGE", "Mật khẩu đã được thay đổi thành công!");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("FAILED_MESSAGE", "Có lỗi xảy ra khi thay đổi mật khẩu");
            dispatcher.forward(req, resp);
        }
    }
}