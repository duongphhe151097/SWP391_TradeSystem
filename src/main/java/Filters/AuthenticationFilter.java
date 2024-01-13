package Filters;

import Models.UserEntity;
import Utils.Annotations.Authentication;
import Utils.Constants.CommonConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebFilter(filterName = "AuthenticationFilter", urlPatterns = "/*")
public class AuthenticationFilter extends BaseFilter implements Filter {
    private static final String HOME_PAGE = "home";

    private ServletContext context;

    public void init(FilterConfig config) {
        this.context = config.getServletContext();
        System.out.println("Init Check Authen");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String fullRequestUrl = request.getRequestURI();
        String requestPath = getRequestPath(fullRequestUrl, request.getContextPath());
        Map<String, String> urlPatters = getMappingUrl(context);

        boolean pathFound = pathFound(requestPath,urlPatters);
        if (!pathFound) {
            request.getRequestDispatcher("/common/notfound.jsp")
                    .forward(request, response);
            return;
        }

        boolean isPublic = getAuthenticationController(requestPath, urlPatters);
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute(CommonConstants.USER_SESSION);

        if (!isPublic && user == null) {
            request.setAttribute("ERROR_MESSAGE", "Bạn cần đăng nhập trước để truy cập!");
            request.getRequestDispatcher("/pages/login.jsp")
                    .forward(request, response);
            return;
        }

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

    private boolean pathFound(String requestPath, Map<String, String> urlPatters){
        for (Map.Entry<String, String> entry : urlPatters.entrySet()) {
            String entryKey = entry.getKey();

            if(entryKey.contains("*")){
                entryKey = entryKey.replace("*", "");
            }

            if(requestPath.endsWith(entryKey) && !(entryKey.equals(".jsp") || entryKey.equals(".jspx"))) return true;
        }

        return false;
    }
}
