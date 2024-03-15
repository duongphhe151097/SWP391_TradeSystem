package Controllers;

import Utils.Annotations.Authorization;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "rejectOrderController", urlPatterns = "/rejectOrder")
@Authorization(role = "", isPublic = false)
public class rejectOrderController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("purchaseError.jsp?message=Transaction%20rejected%20by%20user");
    }
}