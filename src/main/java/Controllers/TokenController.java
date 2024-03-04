/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import Models.ErrorEntity;
import Services.UserService;
import Utils.Annotations.Authorization;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author toden
 */
@WebServlet(name = "TokenController", urlPatterns = {"/TokenController"})
@Authorization(role = "", isPublic = true)
public class TokenController extends BaseController {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/error.jsp");
        // Get the 'id' parameter from the request
        String id = request.getParameter("id");
        // Get the 'Authorization' header from the request
        String authorizationHeader = request.getHeader("Authorization");

        // Check if the 'Authorization' header is not null and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && id != null && !id.isBlank()) {
            // Extract the token from the 'Authorization' header
            String accessToken = authorizationHeader.substring("Bearer ".length());

            // Check if the access token is not empty
            if (!accessToken.isEmpty()) {
                // Check if the access token is still valid (implement your own logic here)
                boolean isTokenValid = isTokenValid(accessToken);

                if (isTokenValid) {
                    // Token is valid, you can perform further actions
                    UUID uid = UUID.fromString(id);
                    userService.UpdateUserStatus(uid, (short) 1);
                    response.sendRedirect("/login");
                } else {
                    // Token is not valid, handle accordingly
                    request.setAttribute("error", new ErrorEntity(404, "Token is not valid"));
                    dispatcher.forward(request, response);
                }
            } else {
                // Handle the case where access token is empty
                request.setAttribute("error", new ErrorEntity(404, "Access token is empty"));
                dispatcher.forward(request, response);
            }
        } else {
            // Handle the case where 'Authorization' header is missing or not in the expected format
            request.setAttribute("error", new ErrorEntity(404, "Invalid or missing 'Authorization' header"));
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    public boolean isTokenValid(String token) {
        try {
            // Parse the JWT token
            JWT jwt = JWTParser.parse(token);

            // Retrieve the claims set from the token
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();

            // Check if the token is not expired
            if (isTokenNotExpired(claimsSet.getExpirationTime())) {

                return true; // Token is valid
            }
        } catch (ParseException e) {
            // Token parsing failed
            e.printStackTrace();
        }

        return false; // Token is either expired or invalid
    }

    private boolean isTokenNotExpired(Date expirationTime) {
        // Check if the expiration time is in the future
        return expirationTime != null && expirationTime.after(new Date());
    }

}
