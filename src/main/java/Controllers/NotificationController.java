package Controllers;

import DataAccess.NotificationRepository;
import Models.Common.Pagination;
import Models.Common.ViewPaging;;
import Models.NotificationEntity;
import Models.ProductEntity;
import Utils.Annotations.Authorization;
import Utils.Constants.UserConstant;
import Utils.Validation.StringValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@WebServlet(name = "NotificationController", urlPatterns = {"/notification"})
@Authorization(role = "", isPublic = true)
public class NotificationController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            UUID userId = (UUID) session.getAttribute(UserConstant.SESSION_USERID);
            String sessionId = session.getId();
            // Perform any additional cleanup actions if needed
            NotificationRepository notificationRepository = new NotificationRepository();

            NotificationRepository.getNotificationByUserId(userId);
            String currentPage = req.getParameter("current");
            String pageSize = req.getParameter("size");
            String pageRange = req.getParameter("range");

            try {
                if (StringValidator.isNullOrBlank(currentPage)
                        || StringValidator.isNullOrBlank(pageSize)
                        || StringValidator.isNullOrBlank(pageRange)) {
                    currentPage = "1";
                    pageSize = "5";
                    pageRange = "5";
                }

                long userCount = notificationRepository.countAllByUser(userId);
                Pagination pagination
                        = new Pagination(userCount, Integer.parseInt(currentPage), Integer.parseInt(pageRange), Integer.parseInt(pageSize));

                int startPage = (pagination.getCurrentPage() - 1) * pagination.getPageSize();
                int endPage = pagination.getPageSize();
                List<NotificationEntity> notification = notificationRepository
                        .getUserNotificationWithPaging(startPage, endPage, userId);

                req.setAttribute("VIEW_PAGING", new ViewPaging<>(notification, pagination));

                req.getRequestDispatcher("/pages/notification.jsp").forward(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                Pagination pagination
                        = new Pagination(0, 1, 5, 10);
                req.setAttribute("VIEW_PAGING", new ViewPaging<ProductEntity>(new ArrayList<>(), pagination));
            }

        }

    }

}