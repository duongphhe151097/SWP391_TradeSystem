package Controllers;

import DataAccess.RoleRepository;
import DataAccess.TokenActivationRepository;
import DataAccess.UserRepository;
import Models.RoleEntity;
import Models.TokenActivationEntity;
import Models.UserEntity;
import Models.UserRoleEntity;
import Services.CaptchaService;
import Services.SendMailService;
import Utils.Annotations.Authorization;
import Utils.Constants.ActivationType;
import Utils.Constants.UserConstant;
import Utils.Generators.StringGenerator;
import Utils.Validation.StringValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(name = "RegisterController", urlPatterns = "/register")
@Authorization(role = "",isPublic = true)
public class RegisterController extends BaseController {
    private CaptchaService captchaService;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        this.captchaService = new CaptchaService();
        this.roleRepository = new RoleRepository();
        this.userRepository = new UserRepository();
    }

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
        String hiddenCaptchaId = req.getParameter("hidden_id");

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
            boolean isValidHiddenCaptcha = hiddenCaptchaId != null && !hiddenCaptchaId.isBlank();

            //Nếu không thỏa mãn điều kiện thì set lỗi để gửi về frontend
            if (!isValidFullname) {
                req.setAttribute("FULLNAME_ERROR", "Tên không hợp lệ!");
            }

            if (!isValidUsername) {
                req.setAttribute("USERNAME_ERROR", "Tên tài khoản không hợp lệ!");
            }

            if (!isValidEmail) {
                req.setAttribute("EMAIL_ERROR", "Email không hợp lệ!");
            }

            if (!isValidPassword) {
                req.setAttribute("PASSWORD_ERROR", "Password phải chứa ít nhất 8 - 32 kí tự, bao gồm chữ hoa, chữ thường và kí tự đặc biệt!");
            }

            if (!isRePasswordMatch) {
                req.setAttribute("REPASSWORD_ERROR", "Password nhập lại không khớp!");
            }

            if (!isValidCaptcha) {
                req.setAttribute("CAPTCHA_ERROR", "Captcha ko đc để trống!");
            }

            if (!isValidHiddenCaptcha) {
                req.setAttribute("CAPTCHA_ERROR", "Captcha không hợp lệ!");
            }

            //Hàm isTrue để check tất cả các biến boolean truyền vào có giá trị là true ko,
            //nếu 1 trong các biến có giá trị khác true đều trả ra false
            boolean isAllValid = StringValidator
                    .isTrue(isValidEmail,
                            isValidPassword,
                            isRePasswordMatch,
                            isValidFullname,
                            isValidUsername,
                            isValidCaptcha,
                            isValidHiddenCaptcha);
            if (!isAllValid) {
                //Chuyển dữ liệu sang trang front end
                req.setAttribute("VAR_FULLNAME", fullname);
                req.setAttribute("VAR_EMAIL", email);
                req.setAttribute("VAR_USERNAME", username);
                dispatcher.forward(req, resp);
                return;
            }

            //Check captcha
            if (!captchaService.isValidCaptcha(captcha, hiddenCaptchaId)) {
                req.setAttribute("VAR_FULLNAME", fullname);
                req.setAttribute("VAR_EMAIL", email);
                req.setAttribute("VAR_USERNAME", username);
                req.setAttribute("CAPTCHA_ERROR", "Captcha bạn nhập không đúng!");
                dispatcher.forward(req, resp);
                return;
            }

            //Check xem username đã tồn tại hay chưa
            Optional<UserEntity> existUser = userRepository.getUserByUsername(username);
            if (existUser.isPresent()) {
                req.setAttribute("VAR_FULLNAME", fullname);
                req.setAttribute("VAR_EMAIL", email);
                req.setAttribute("VAR_USERNAME", username);
                req.setAttribute("USERNAME_ERROR", "Tên tài khoản đã tồn tại!");
                dispatcher.forward(req, resp);
                return;
            }

            //Check xem email đã tồn tại hay chưa
            Optional<UserEntity> existEmail = userRepository.getUserByEmail(email);
            if (existEmail.isPresent()) {
                req.setAttribute("VAR_FULLNAME", fullname);
                req.setAttribute("VAR_EMAIL", email);
                req.setAttribute("VAR_USERNAME", username);
                req.setAttribute("EMAIL_ERROR", "Email đã tồn tại!");
                dispatcher.forward(req, resp);
                return;
            }

            // Tạo ra salt để mã hóa, thuật toán mã hóa pbkdf2
            String salt = StringGenerator.generateRandomString(50);
            String hashedPassword = StringGenerator.hashingPassword(password, salt);

            //Tạo user entity từ dữ liệu nhận đc
            //Ở đây đang sử dụng builder, tương tự như sử dụng constructor hoặc method set nhưng gọn hơn
            Optional<RoleEntity> roles = roleRepository.getRoleByName("USER");
            int roleId = 1;
            if(roles.isPresent()){
                roleId = roles.get().getRoleId();
            }

            UserEntity userEntity = UserEntity
                    .builder()
                    .id(UUID.randomUUID())
                    .username(username)
                    .password(hashedPassword)
                    .salt(salt)
                    .email(email)
                    .fullName(fullname)
                    .status(UserConstant.PENDING)
                    .attemp((short) 0)
                    .avatar("")
                    .balance(BigDecimal.ZERO)
                    .address("Không có")
                    .phoneNumber("Không có")
                    .rating(0)
                    .build();


            //Kiểu optional là đối tượng trả ra có thể là null hoặc not null
            Optional<UserEntity> user = userRepository.addUser(userEntity);
            if(user.isEmpty()){
                req.setAttribute("FAILED_MESSAGE", "Có lỗi xảy ra khi tạo tài khoản!");
                dispatcher.forward(req, resp);
                return;
            }

            roleRepository.addUserRole(user.get().getId(), roleId);
            //Send mail
            String activeToken = StringGenerator.generateRandomString(50);
            String activationLink = getBaseURL(req) + "/activate?t=" + activeToken;
            SendMailService.sendMail(email, "TradeSystem", "Active link: " + getBaseURL(req) + "/active?t=" + activeToken);

            //Save active token
            TokenActivationRepository tokenActivationRepository = new TokenActivationRepository();
            TokenActivationEntity tokenActivationEntity = TokenActivationEntity.builder()
                    .id(UUID.randomUUID())
                    .token(activeToken)
                    .userId(user.get().getId())
                    .type(ActivationType.ACTIVE_REQUEST)
                    .isUsed(false)
                    .createAt(LocalDateTime.now())
                    .expriedAt(LocalDateTime.now().plusDays(1))
                    .build();
            tokenActivationRepository.addToken(tokenActivationEntity);

            req.setAttribute("SUCCESS_MESSAGE", "Tài khoản đã được tạo! Hãy truy cập email để kích hoạt tài khoản!");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("FAILED_MESSAGE", "Có lỗi xảy ra khi tạo tài khoản!");
            dispatcher.forward(req, resp);
        }
    }
}
