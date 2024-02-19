package Filters;

import DataAccess.RoleRepository;
import Dtos.SessionDto;
import Models.RoleEntity;
import Services.SessionService;
import Utils.Annotations.Authorization;
import Utils.Constants.CommonConstants;
import Utils.Constants.UserConstant;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebFilter(filterName = "AuthorizeFilter", urlPatterns = "/*")
public class AuthorizeFilter extends BaseFilter implements Filter {
    private ServletContext context;
    private RoleRepository roleRepository;
    private SessionService sessionService;

    public void init(FilterConfig config) throws ServletException {
        this.context = config.getServletContext();
        this.roleRepository = new RoleRepository();
        this.sessionService = new SessionService();
        System.out.println("Init Filter Check Authorization");

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        this.context = null;
        this.roleRepository = null;
        this.sessionService = null;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
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
                publicPageProcess(chain, request, response, requestPath);
            } else {
                privatePageProcess(chain, request, response, requestPath, roles);
            }
        } finally {
            UsernameHolder.clearUserName();
        }
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
                    if (role.isBlank()) role = "";

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
        // If input is null or blank return empty array
        if (roleRequire == null || roleRequire.isBlank()) return new String[]{};
        // If role string contains "," delim, split it
        if (roleRequire.contains(",")) return roleRequire.split(",");

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
            //
            if (entryKey.contains("*")) entryKey = entryKey.replace("*", "");
            //Check request url have file extension or not
            String regexFileExtension = "\\.[a-zA-Z0-9]+$";
            Pattern regexFileExtensionPattern = Pattern.compile(regexFileExtension);
            Matcher matcher = regexFileExtensionPattern.matcher(requestPath);
            if (matcher.find()) {
                if (requestPath.endsWith(".jsp") || requestPath.endsWith(".jspx")) return false;

                if (requestPath.endsWith(entryKey)) return true;
            } else {
                if (requestPath.equals(entryKey)) return true;
            }
        }

        return false;
    }

    public void publicPageProcess(FilterChain chain, HttpServletRequest request, HttpServletResponse response, String requestPath) throws ServletException, IOException {
//        SessionDto sessionInfo = sessionService.isValidSession(request);
        HttpSession session = request.getSession(false);
        String JSESSIONID = getCookieValue(request, CommonConstants.SESSION_COOKIE_KEY);

        String[] exceptionPath = new String[]{"/login", "/register", "/forgot"};

        if ((session != null && !JSESSIONID.isBlank()) && Arrays.asList(exceptionPath).contains(requestPath)) {
            if(session.getAttribute(UserConstant.SESSION_USERID) == null){
                session.invalidate();
                chain.doFilter(request, response);
                return;
            }

            if(request.getMethod().equalsIgnoreCase("post") && requestPath.equals("/login")){
                session.invalidate();
                chain.doFilter(request, response);
                return;
            }
            response.sendRedirect("home");
            return;
        }

        chain.doFilter(request, response);
    }

    public void privatePageProcess(FilterChain chain, HttpServletRequest request, HttpServletResponse response, String requestPath, String[] roles) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");

        //Case page non-public
        SessionDto sessionInfo = sessionService.isValidSession(request);
        if (!sessionInfo.isValid()) {
            request.setAttribute("FAILED_MESSAGE", sessionInfo.getMessage());
            dispatcher.forward(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute(UserConstant.SESSION_USERNAME);
        if (username != null) UsernameHolder.setUserName(username);

        if (roles.length == 0) {
            chain.doFilter(request, response);
            return;
        }

        Optional<Set<RoleEntity>> userRole = roleRepository.getRoleByUserId(sessionInfo.getUserId());
        if (userRole.isEmpty() || !containRole(userRole.get(), roles)) {
            request.setAttribute("STATUS_CODE", "403");
            request.setAttribute("ERROR_MESSAGE", "Bạn không có quyền truy cập!");
            request.getRequestDispatcher("/common/notfound.jsp")
                    .forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }
}
