package Controllers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class BaseController extends HttpServlet {
    protected void printJson(HttpServletRequest request, HttpServletResponse response, String message) {
        try{
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(message);
            out.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getBaseURL(HttpServletRequest request){
        StringBuffer buffer = request.getRequestURL();
        buffer.substring(0, buffer.length() - 9);

        return buffer.toString();
    }
}
