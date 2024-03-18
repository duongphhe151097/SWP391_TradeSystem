package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class BaseController extends HttpServlet {
    protected void printJson(HttpServletResponse response, String message) {
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.write(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getBaseURL(HttpServletRequest request) {
        StringBuffer buffer = request.getRequestURL();
        return buffer.toString().replace(request.getRequestURI(), request.getContextPath());
    }
}
