package Controllers;

import Models.Common.Version;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "VersionController", value = "/version")
//@Authorization(pageSignature = "ACT_VERSION")
//@Authentication(isAuthen = true)
public class VersionController extends BaseController {

    private final Version version;
    private final Gson gson;

    public VersionController() {
        version = new Version();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        printJson(request, response, AppSettings.getAppName());

//        try {
//            SendMailService.executeSendEmail("duongphhe151097@fpt.edu.vn", "duongphhe151097@fpt.edu.vn", "Test", "<h1>Send mail from java app!</h1>");
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        }
        printJson(request, response, "V1.1.1");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void printJson(HttpServletRequest request, HttpServletResponse response, String message) {
        super.printJson(request, response, message);
    }
}
