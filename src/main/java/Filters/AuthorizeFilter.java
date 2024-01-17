package Filters;

import Models.UserEntity;
import Utils.Annotations.Authorization;
import Utils.Constants.CommonConstants;
import Utils.Constants.UserConstant;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@WebFilter(filterName = "AuthorizeFilter", urlPatterns = "/*")
public class AuthorizeFilter extends BaseFilter implements Filter {
    private ServletContext context;

    public void init(FilterConfig config) throws ServletException {
        this.context = config.getServletContext();
        System.out.println("Init Check Authorization");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String fullRequestUrl = request.getRequestURI();
        String requestPath = getRequestPath(fullRequestUrl, request.getContextPath());

        Map<String, String> urlPatters = getMappingUrl(context);
        String roleRequire = getAuthorizationController(requestPath, urlPatters);
        boolean validAuthorize = false;

//        HttpSession session = request.getSession(false);
//        if(session == null){
//
//        }
//
//        String userId = (String) session.getAttribute(UserConstant.SESSION_USERID);
//        if (validAuthorize) {
//            System.out.println("Valid");
//        } else {
//            System.out.println("Not Valid");
//        }

        chain.doFilter(request, response);
    }

    private String getAuthorizationController(String requestPath, Map<String, String> urlPatters) {
        String pageSignature = null;
        for (Map.Entry<String, String> entry : urlPatters.entrySet()) {
            if (entry.getKey().equals(requestPath)) {
                try {
                    Class<?> clazz = Class.forName(entry.getValue());
                    Authorization authorization = clazz.getDeclaredAnnotation(Authorization.class);
                    pageSignature = Optional.ofNullable(authorization)
                            .map(Authorization::role)
                            .orElse(CommonConstants.DEFAULT);

                    if (pageSignature.trim().equals("")) {
                        pageSignature = CommonConstants.DEFAULT;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return pageSignature;
    }
}
