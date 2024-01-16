package Filters;

import Models.UserEntity;
import Utils.Annotations.Authentication;
import Utils.Constants.CommonConstants;
import Utils.Constants.UserConstant;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = "/*")
public class AuthenticationFilter extends BaseFilter implements Filter {
    private ServletContext context;

    public void init(FilterConfig config) {
        this.context = config.getServletContext();
        System.out.println("Init Filter Check Authentication");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String fullRequestUrl = request.getRequestURI();
        String requestPath = getRequestPath(fullRequestUrl, request.getContextPath());
        Map<String, String> urlPatters = getMappingUrl(context);

        boolean pathFound = pathFound(requestPath, urlPatters);
        if (!pathFound) {
            request.getRequestDispatcher("/common/notfound.jsp")
                    .forward(request, response);
            return;
        }

        boolean isPublic = getAuthenticationController(requestPath, urlPatters);

        //If is public page => next
        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        //Case page non-public
        //Todo: CÓ THỂ VIẾT LẠI LOGIC ĐOẠN NÀY DÙNG COOKIES
        HttpSession session = request.getSession(false);
        String JSESSIONID = getCookieValue(request, CommonConstants.SESSION_COOKIE_KEY);
        if (session == null || JSESSIONID.isBlank()) {
            request.setAttribute("FAILED_MESSAGE", "Bạn cần đăng nhập trước để truy cập!");
            request.getRequestDispatcher("/pages/login.jsp")
                    .forward(request, response);
            return;
        }

        String userId = (String) session.getAttribute(UserConstant.SESSION_USERID);


        //Todo:
        //Tìm trong db xem tồn tại session id tương ứng với JSESSIONID hiện tại ko
        //Nếu tồn tại => đi tiếp
        //Nếu ko => redirect sang login "Đã có thiết bị khác đăng nhập! "

        chain.doFilter(request, response);
    }

    private boolean getAuthenticationController(String requestPath, Map<String, String> urlPatters) {
        boolean isPublic = true;
        for (Map.Entry<String, String> entry : urlPatters.entrySet()) {
            if (entry.getKey().equals(requestPath)) {
                try {
                    Class<?> clazz = Class.forName(entry.getValue());
                    Authentication authentication = clazz.getDeclaredAnnotation(Authentication.class);
                    isPublic = Optional.ofNullable(authentication)
                            .map(Authentication::isPublic)
                            .orElse(true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return isPublic;
    }

    private boolean pathFound(String requestPath, Map<String, String> urlPatters) {
        for (Map.Entry<String, String> entry : urlPatters.entrySet()) {
            String entryKey = entry.getKey();

            if (entryKey.contains("*")) {
                entryKey = entryKey.replace("*", "");
            }

            if (requestPath.endsWith(entryKey) && !(entryKey.equals(".jsp") || entryKey.equals(".jspx"))) return true;
        }

        return false;
    }

    private String getCookieValue(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if(cookie.getName().equals(key)) return cookie.getValue();
        }

        return "";
    }
}
