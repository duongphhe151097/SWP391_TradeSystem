package Controllers;

import DataAccess.TokenActivationRepository;
import DataAccess.UserRepository;
import Models.TokenActivationEntity;
import Models.UserEntity;
import Utils.Generators.StringGenerator;
import Utils.Validation.StringValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "ChangeController", urlPatterns = "/change")
public class ChangeController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/pages/change.jsp").forward(req, resp);
    }
    @Override

        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String userIdStr = request.getParameter("userId");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");
            String reNewPassword = request.getParameter("reNewPassword");

            try {
                boolean isUsername = StringValidator.isValidUsername(oldPassword);
                boolean isPassword = StringValidator.isValidPassword(newPassword);
//                boolean isValidCaptcha = captcha != null && !captcha.isBlank();
//                boolean isValidHiddenCaptcha = hiddenCaptchaId != null && !hiddenCaptchaId.isBlank();
                UUID userId = UUID.fromString(userIdStr);

                UserRepository userRepository = new UserRepository();
                Optional<UserEntity> userOptional = userRepository.getUserById(userId);

                if (userOptional.isPresent()) {
                    UserEntity user = userOptional.get();
                    String storedPassword = user.getPassword();

                    // Kiểm tra xem mật khẩu cũ có khớp không
                    if (storedPassword.equals(oldPassword)) {

                        request.setAttribute("resultMessage", "Thay đổi mật khẩu thành công!");
                    } else {
                        request.setAttribute("resultMessage", "Mật khẩu cũ không đúng!");
                    }
                } else {
                    request.setAttribute("resultMessage", "Người dùng không tồn tại!");
                }
            } catch (IllegalArgumentException e) {
                request.setAttribute("resultMessage", "ID người dùng không hợp lệ!");
            }


//            RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/result.jsp");
//            dispatcher.forward(request, response);
        }
    }
