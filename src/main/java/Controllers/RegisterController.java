package Controllers;

import DataAccess.CaptchaRepository;
import DataAccess.DbFactory;
import DataAccess.TokenActivationRepository;
import DataAccess.UserRepository;
import ExternalServices.SendMailService;
import Models.CaptchaEntity;
import Models.TokenActivationEntity;
import Models.UserEntity;
import Utils.Generators.StringGenerator;
import Utils.Validation.StringValidator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "RegisterController", urlPatterns = "/register")
public class RegisterController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get data send from frontend
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String rePassword = req.getParameter("repassword");
        String captcha = req.getParameter("captcha");

        //Tạo Request Dispatcher, chỉ nên tạo 1 lần cho mỗi method doGet hoặc doPost, tránh lặp code
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/register.jsp");

        try {
            //Check, validate các trường nhận từ frontend
            boolean isValidEmail = StringValidator.isValidEmail(email);
            boolean isValidPassword = StringValidator.isValidPassword(password);
            boolean isRePasswordMatch = password.equals(rePassword);
            boolean isValidFullname = StringValidator.isValidFullname(fullname);
            boolean isValidUsername = StringValidator.isValidUsername(username);
            boolean isValidCaptcha = captcha != null && !captcha.isBlank();

            //Nếu không thỏa mãn điều kiện thì set lỗi để gửi về frontend
            if (!isValidFullname) {
                req.setAttribute("FULLNAME_ERROR", "Tên không hợp lệ");
            }

            if (!isValidUsername) {
                req.setAttribute("USERNAME_ERROR", "Tên tài khoản không hợp lệ");
            }

            if (!isValidEmail) {
                req.setAttribute("EMAIL_ERROR", "Email không hợp lệ!");
            }

            if (!isValidPassword) {
                req.setAttribute("PASSWORD_ERROR", "Password phải chứa ít nhất 8 kí tự!");
            }

            if (!isRePasswordMatch) {
                req.setAttribute("REPASSWORD_ERROR", "Password nhập lại không khớp!");
            }

            if (!isValidCaptcha){
                req.setAttribute("CAPTCHA_ERROR", "Captcha ko đc để trống!");
            }

            //Hàm isTrue để check tất cả các biến boolean truyền vào có giá trị là true ko,
            //nếu 1 trong các biến có giá trị khác true đều trả ra false
            boolean isAllValid = StringValidator
                    .isTrue(isValidEmail,
                            isValidPassword,
                            isRePasswordMatch,
                            isValidFullname,
                            isValidUsername,
                            isValidCaptcha);
            if (!isAllValid) {
                //Chuyển dữ liệu sang trang front end
                req.setAttribute("VAR_FULLNAME", fullname);
                req.setAttribute("VAR_EMAIL", email);
                req.setAttribute("VAR_USERNAME", username);
                dispatcher.forward(req, resp);
                return;
            }

            //Check captcha
            CaptchaRepository captchaRepository = new CaptchaRepository();
            Optional<CaptchaEntity> captchaEntity = captchaRepository.getCaptchaById(captcha);

            if(captchaEntity.isEmpty()){
                req.setAttribute("VAR_FULLNAME", fullname);
                req.setAttribute("VAR_EMAIL", email);
                req.setAttribute("VAR_USERNAME", username);
                req.setAttribute("CAPTCHA_ERROR", "Captcha bạn nhập không đúng!");
                dispatcher.forward(req,resp);
                return;
            }

            //Delete captcha
            captchaRepository.deleteCaptcha(captcha);

            //Khởi tạo UserRepo
            UserRepository repository = new UserRepository();

            //Check xem username đã tồn tại hay chưa
            Optional<UserEntity> existUser = repository.getUserByUsername(username);
            if(existUser.isPresent()){
                req.setAttribute("VAR_FULLNAME", fullname);
                req.setAttribute("VAR_EMAIL", email);
                req.setAttribute("VAR_USERNAME", username);
                req.setAttribute("USERNAME_ERROR", "Tên tài khoản đã tồn tại");
                dispatcher.forward(req, resp);
                return;
            }

            //Check xem email đã tồn tại hay chưa
            Optional<UserEntity> existEmail = repository.getUserByEmail(username);
            if(existEmail.isPresent()){
                req.setAttribute("VAR_FULLNAME", fullname);
                req.setAttribute("VAR_EMAIL", email);
                req.setAttribute("VAR_USERNAME", username);
                req.setAttribute("EMAIL_ERROR", "Email đã tồn tại");
                dispatcher.forward(req, resp);
                return;
            }

            // Tạo ra salt để mã hóa, thuật toán mã hóa pbkdf2
            String salt = StringGenerator.generateRandomString(50);
            String hashedPassword = StringGenerator.hashingPassword(password, salt);

            //Tạo user entity từ dữ liệu nhận đc
            //Ở đây đang sử dụng builder, tương tự như sử dụng constructor hoặc method set nhưng gọn hơn
            UserEntity userEntity = UserEntity
                    .builder()
                    .id(UUID.randomUUID())
                    .username(username)
                    .password(hashedPassword)
                    .salt(salt)
                    .email(email)
                    .fullName(fullname)
                    .status((short) 0)
                    .avatar("")
                    .balance(BigDecimal.ZERO)
                    .address("")
                    .phoneNumber("")
                    .rating(0)
                    .build();
            userEntity.setCreateBy("");

            //Kiểu optional là đối tượng trả ra có thể là null hoặc not null
            UserEntity user = repository.addUser(userEntity).get();

            //Send mail
            String activeToken = StringGenerator.generateRandomString(50);
            SendMailService.sendMail(email, "TradeSystem", "Active link: " + getBaseURL(req) + "/active?t=" + activeToken);

            //Save active token
            TokenActivationRepository tokenActivationRepository = new TokenActivationRepository();
            tokenActivationRepository.addToken(new TokenActivationEntity(activeToken, user.getId(), LocalDateTime.now(), LocalDateTime.now().plusDays(7)));

            req.setAttribute("SUCCESS_MESSAGE", "Tài khoản đã được tạo! Hãy truy cập email để kích hoạt tài khoản!");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("FAILED_MESSAGE", "Có lỗi xảy ra khi tạo tài khoản");
            dispatcher.forward(req, resp);
        }
    }
}
