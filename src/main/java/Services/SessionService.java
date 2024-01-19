package Services;

import DataAccess.SessionManagerRepository;
import Dtos.SessionDto;
import Models.SessionManagerEntity;
import Utils.Constants.CommonConstants;
import Utils.Constants.UserConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import java.util.UUID;

public class SessionService {
    private final SessionManagerRepository sessionManagerRepository;

    public SessionService() {
        this.sessionManagerRepository = new SessionManagerRepository();
    }

    public SessionDto isValidSession(HttpServletRequest request){
        SessionDto commonErrorResponse = SessionDto.builder()
                .isValid(false)
                .message("Bạn cần đăng nhập trước để truy cập!")
                .session(null)
                .userId(null)
                .build();
        try {
            HttpSession session = request.getSession(false);
            String JSESSIONID = getCookieValue(request, CommonConstants.SESSION_COOKIE_KEY);

            if(session == null || JSESSIONID.isBlank())
                return commonErrorResponse;

            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            if (userId == null)
                return commonErrorResponse;

            Optional<SessionManagerEntity> sessionManagerEntity = sessionManagerRepository
                    .getSessionByUserId(userId);

            if (sessionManagerEntity.isEmpty())
                return commonErrorResponse;

            if (!sessionManagerEntity.get().getSessionId().equals(JSESSIONID)){
                session.invalidate();
                return commonErrorResponse;
            }

            return SessionDto.builder()
                    .isValid(true)
                    .message("Valid session")
                    .session(session)
                    .userId(userId)
                    .build();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return commonErrorResponse;
    }

    public String getCookieValue(HttpServletRequest request, String key) {
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
