package Filters;

import DataAccess.RoleRepository;
import DataAccess.SessionManagerRepository;
import Models.RoleEntity;
import Models.SessionManagerEntity;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import Utils.Constants.CommonConstants;
import Utils.Constants.UserConstant;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebFilter(filterName = "AuthorizeFilter", urlPatterns = "/*")
public class AuthorizeFilter extends BaseFilter implements Filter {
    private ServletContext context;
    private RoleRepository roleRepository;
    private SessionManagerRepository sessionManagerRepository;

    public void init(FilterConfig config) throws ServletException {
        this.context = config.getServletContext();
        this.roleRepository = new RoleRepository();
        this.sessionManagerRepository = new SessionManagerRepository();
        System.out.println("Init Check Authorization");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String fullRequestUrl = request.getRequestURI();
        String requestPath = getRequestPath(fullRequestUrl, request.getContextPath());
        Map<String, String> urlPatters = getMappingUrl(context);

        //Check url is exist or not
        boolean pathFound = pathFound(requestPath, urlPatters);
        if (!pathFound) {
            request.setAttribute("STATUS_CODE", "404");
            request.setAttribute("ERROR_MESSAGE", "Trang không tồn tại!");
            request.getRequestDispatcher("/common/notfound.jsp")
                    .forward(request, response);
            return;
        }

        Pair<String, Boolean> authorization = getAuthorizationController(requestPath, urlPatters);
        String[] roles = getAllRoleFromString(authorization.getValue0());
        boolean isPublic = authorization.getValue1();

        //If is public page => next
        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        //Case page non-public
        HttpSession session = request.getSession(false);
        String JSESSIONID = getCookieValue(request, CommonConstants.SESSION_COOKIE_KEY);

        //If request to /login JSESSIONID must be empty
        if (requestPath.endsWith("/login")) {
            JSESSIONID = "";
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");

        if (session == null || JSESSIONID.isBlank()) {
            request.setAttribute("FAILED_MESSAGE", "Bạn cần đăng nhập trước để truy cập!");
            dispatcher.forward(request, response);
            return;
        }

        //Session id exist but, not exist userId in session
        UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
        if (userId == null) {
            request.setAttribute("FAILED_MESSAGE", "Bạn cần đăng nhập trước để truy cập!");
            dispatcher.forward(request, response);
            return;
        }

        Optional<SessionManagerEntity> sessionManagerEntity = sessionManagerRepository
                .getSessionByUserId(userId);

        if (sessionManagerEntity.isEmpty()) {
            request.setAttribute("FAILED_MESSAGE", "Bạn cần đăng nhập trước để truy cập!");
            dispatcher.forward(request, response);
            return;
        }

        if (!sessionManagerEntity.get().getSessionId().equals(JSESSIONID)) {
            request.setAttribute("FAILED_MESSAGE", "Đã có thiết bị khác đăng nhập!");
            dispatcher.forward(request, response);
            return;
        }

        if (roles.length == 0) {
            chain.doFilter(request, response);
            return;
        }

        Optional<Set<RoleEntity>> userRole = roleRepository.getRoleByUserId(userId);
        if (userRole.isEmpty() || !containRole(userRole.get(), roles)) {
            request.setAttribute("STATUS_CODE", "403");
            request.setAttribute("ERROR_MESSAGE", "Bạn không có quyền truy cập!");
            request.getRequestDispatcher("/common/notfound.jsp")
                    .forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private Pair<String, Boolean> getAuthorizationController(String requestPath, Map<String, String> urlPatters) {
        String role = "";
        boolean isPublic = false;
        for (Map.Entry<String, String> entry : urlPatters.entrySet()) {
            if (entry.getKey().equals(requestPath)) {
                try {
                    Class<?> clazz = Class.forName(entry.getValue());
                    Authorization authorization = clazz.getDeclaredAnnotation(Authorization.class);
                    role = Optional.ofNullable(authorization)
                            .map(Authorization::role)
                            .orElse("");
                    if (role.isBlank()) {
                        role = "";
                    }

                    isPublic = Optional.ofNullable(authorization)
                            .map(Authorization::isPublic)
                            .orElse(false);

                    return new Pair<>(role, isPublic);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Pair<>(role, true);
    }

    private String[] getAllRoleFromString(String roleRequire) {
        if (roleRequire == null || roleRequire.isBlank())
            return new String[]{};

        if (roleRequire.contains(",")) {
            return roleRequire.split(",");
        }

        return new String[]{roleRequire};
    }

    private boolean containRole(Set<RoleEntity> userRoles, String[] roleToChecks) {
        return Arrays.stream(roleToChecks)
                .anyMatch(roleToCheck -> userRoles.stream().anyMatch(role ->
                        role.getRoleName().equals(roleToCheck)));
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

    private String getCookieValue(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) return cookie.getValue();
        }

        return "";
    }
}
