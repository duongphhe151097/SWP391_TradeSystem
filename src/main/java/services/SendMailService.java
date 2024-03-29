package services;

import utils.settings.AppSettings;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class SendMailService {

    public static boolean sendMail(String to, String subject, String content) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); //TLS
            props.put("mail.mime.charset","text/plain; charset=UTF-8");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(AppSettings.getMailUser(), AppSettings.getMailPassword());
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("duongphhe151097@fpt.edu.vn"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
}