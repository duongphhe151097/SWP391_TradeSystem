package Filters;

import DataAccess.RoleRepository;
import Dtos.SessionDto;
import Models.RoleEntity;
import Services.SessionService;
import Utils.Annotations.Authorization;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@WebFilter(filterName = "AuthorizeFilter", urlPatterns = "/*")
public class AuthorizeFilter extends BaseFilter implements Filter {
    private ServletContext context;
    private RoleRepository roleRepository;
    private SessionService sessionService;

    public void init(FilterConfig config) throws ServletException {
        this.context = config.getServletContext();
        this.roleRepository = new RoleRepository();
        this.sessionService = new SessionService();
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
            publicPageProcess(chain, request, response, requestPath);
        } else {
            privatePageProcess(chain, request, response, requestPath, roles);
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
            if (requestPath.endsWith(entryKey) && !(entryKey.equals(".jsp") || entryKey.equals(".jspx"))) return true;
        }

        return false;
    }

    public void publicPageProcess(FilterChain chain, HttpServletRequest request, HttpServletResponse response, String requestPath) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/login.jsp");
        SessionDto sessionInfo = sessionService.isValidSession(request);
        String[] exceptionPath = new String[]{"/login", "/register", "/forgot"};

        if (sessionInfo.isValid() && Arrays.stream(exceptionPath).anyMatch(requestPath::endsWith)) {
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
