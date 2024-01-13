package ExternalServices;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.*;
import org.apache.commons.codec.binary.Base64;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class SendMailService {

    private static final String APPLICATION_NAME = "MyApp";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);

    public static void main(String[] args) throws Exception {
        // Build a new authorized Gmail API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Build the email message
        MimeMessage email = createEmail("duongphhe151097@fpt.edu.vn", "duongphhe151097@fpt.edu.vn", "Chào idol Tuấn", "<h1>Testttt</h1>", null);

        // Send the email message
        sendEmail(service, "me", email);
    }

    public static void executeSendEmail(String from, String to, String subject, String content) throws MessagingException, IOException, GeneralSecurityException {
        // Build a new authorized Gmail API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        // Build the email message
        MimeMessage email = createEmail(to, from, subject, content, null);

        // Send the email message
        sendEmail(service, "me", email);
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException, GeneralSecurityException {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = GoogleClientSecrets
                .load(JSON_FACTORY, new InputStreamReader(SendMailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH)));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static MimeMessage createEmail(String to, String from, String subject, String body, String file) throws MessagingException, IOException, AddressException {
        Properties props = new Properties();
        jakarta.mail.Session session = jakarta.mail.Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        if (file == null) {
            // Create the email body as a plain text message.
            Multipart multipart = new MimeMultipart();
            BodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body, "text/html");
            multipart.addBodyPart(htmlPart);

            email.setContent(multipart);
        } else {
            // Create the email body as a multipart message containing both plain text and HTML parts,
            // plus an attachment.
            Multipart multipart = new MimeMultipart();

            // Create the plain text message part.
            BodyPart textPart = new MimeBodyPart();
            textPart.setContent(body, "text/plain");
            multipart.addBodyPart(textPart);

            // Create the HTML message part.
            BodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body, "text/html");
            multipart.addBodyPart(htmlPart);

            // Create the attachment part.
            BodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(new File(file).getName());
            multipart.addBodyPart(attachmentPart);

            // Set the email body to the multipart message.
            email.setContent(multipart);
        }

        return email;
    }

    /**
     * Send an email message.
     *
     * @param service The Gmail API client service.
     * @param userId  The user's email address. The special value "me"
     *                can be used to indicate the authenticated user.
     * @param email   The MimeMessage object representing the email message.
     */
    private static boolean sendEmail(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
        try{
            Message message = createMessageWithEmail(email);
            message = service.users().messages().send(userId, message).execute();

            System.out.println("Message Id: " + message.getId());
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Create a Message from an email.
     *
     * @param email The MimeMessage object representing the email message.
     * @return The Message object representing the email message.
     */
    private static Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        email.writeTo(bytes);
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}