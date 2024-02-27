package services;

import dataAccess.CaptchaRepository;
import models.CaptchaEntity;
import utils.convert.StringConvertor;
import com.mewebstudio.captcha.Captcha;
import com.mewebstudio.captcha.Config;
import com.mewebstudio.captcha.GeneratedCaptcha;
import com.mewebstudio.captcha.exception.FontLoadException;
import com.mewebstudio.captcha.util.RandomStringGenerator;
import jakarta.servlet.ServletContext;
import org.javatuples.Pair;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class CaptchaService {
    private Optional<Pair<String, BufferedImage>> generateCaptchaImage(ServletContext context) {
        try {
            Config captchaConfig = new Config();
            captchaConfig.setWidth(150);
            captchaConfig.setHeight(50);
            captchaConfig.setDark(true);
            captchaConfig.setFonts(new String[]{
                    context.getRealPath("/WEB-INF/classes/fonts/Roboto-Regular.ttf")
            });

            Captcha captcha = new Captcha(captchaConfig);
            captcha.setRandomStringGenerator(new RandomStringGenerator(5, true));

            GeneratedCaptcha generatedCaptcha = captcha.generate();
            return Optional.of(new Pair<>(generatedCaptcha.getCode(), generatedCaptcha.getImage()));
        } catch (FontLoadException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<Pair<UUID, String>> generateCaptcha(ServletContext context) {
        try {
            Optional<Pair<String, BufferedImage>> bufferedImage = generateCaptchaImage(context);

            if (bufferedImage.isEmpty()) return Optional.empty();
            String base64Img = StringConvertor
                    .imgToBase64String(bufferedImage.get().getValue1(), "png");

            CaptchaRepository captchaRepository = new CaptchaRepository();
            CaptchaEntity captcha = CaptchaEntity.builder()
                    .captchaId(bufferedImage.get().getValue0())
                    .data(base64Img)
                    .expriedAt(LocalDateTime.now().plusMinutes(5))
                    .build();
            Optional<CaptchaEntity> savedCaptcha = captchaRepository.addCaptcha(captcha);

            if (savedCaptcha.isEmpty()) return Optional.empty();

            return Optional.of(new Pair<>(savedCaptcha.get().getId(), "data:image/png;base64, " + base64Img));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean isValidCaptcha(String captcha, String id) {
        try {
            CaptchaRepository captchaRepository = new CaptchaRepository();
            Optional<CaptchaEntity> captchaEntity = captchaRepository
                    .getCaptchaById(captcha, UUID.fromString(id));

            if (captchaEntity.isEmpty()
                    || captchaEntity.get().getExpriedAt().isBefore(LocalDateTime.now()))
                return false;

            captchaRepository.deleteCaptcha(captcha, UUID.fromString(id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
