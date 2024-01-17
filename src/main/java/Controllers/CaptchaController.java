package Controllers;

import DataAccess.CaptchaRepository;
import Dtos.CaptchaDto;
import Models.CaptchaEntity;
import Services.CaptchaService;
import Utils.Annotations.Authentication;
import Utils.Constants.CommonConstants;
import Utils.Convert.StringConvertor;
import com.google.gson.Gson;
import com.mewebstudio.captcha.Captcha;
import com.mewebstudio.captcha.Config;
import com.mewebstudio.captcha.GeneratedCaptcha;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.javatuples.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "CaptchaController", urlPatterns = "/captcha")
@Authentication(isPublic = true)
public class CaptchaController extends BaseController {
    private CaptchaService captchaService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.captchaService = new CaptchaService();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Optional<Pair<UUID, String>> captcha = captchaService.generateCaptcha(getServletContext());
            if(captcha.isEmpty()){
                resp.setStatus(500);
                return;
            }
            CaptchaDto captchaDto = CaptchaDto.builder()
                    .captchaId(String.valueOf(captcha.get().getValue0()))
                    .captchaImg(captcha.get().getValue1())
                    .build();
            String json = gson.toJson(captchaDto);
            resp.setStatus(200);
            printJson(resp ,json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
