package Controllers;

import DataAccess.CaptchaRepository;
import Models.CaptchaEntity;
import Utils.Constants.CommonConstants;
import Utils.Convert.StringConvertor;
import com.mewebstudio.captcha.Captcha;
import com.mewebstudio.captcha.Config;
import com.mewebstudio.captcha.GeneratedCaptcha;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "CaptchaController", urlPatterns = "/captcha")
public class CaptchaController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (OutputStream stream = resp.getOutputStream()) {
            resp.setContentType("image/png");

            Config captchaConfig = new Config();
            captchaConfig.setWidth(150);
            captchaConfig.setHeight(50);
            captchaConfig.setDark(true);
            captchaConfig.setFonts(new String[]{
                    getServletContext().getRealPath("/WEB-INF/classes/fonts/Roboto-Regular.ttf")
            });

            Captcha captcha = new Captcha(captchaConfig);

            GeneratedCaptcha generatedCaptcha = captcha.generate();
            BufferedImage captchaImage = generatedCaptcha.getImage();
            String base64Img = StringConvertor.imgToBase64String(captchaImage, "png");

            CaptchaRepository captchaRepository = new CaptchaRepository();
            CaptchaEntity entity = CaptchaEntity
                    .builder()
                    .captchaId(generatedCaptcha.getCode())
                    .data(base64Img)
                    .createBy(CommonConstants.DEFAULT_USER)
                    .build();
            captchaRepository.addCaptcha(entity);

            ImageIO.write(captchaImage, "png", stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
