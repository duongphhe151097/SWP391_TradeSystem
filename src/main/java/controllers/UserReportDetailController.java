package controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.UserReportRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.UserReportEntity;
import utils.annotations.Authorization;
import utils.constants.ReportConstant;
import utils.validation.StringValidator;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "UserReportDetailController", urlPatterns = "/report/detail")
@Authorization(role = "USER", isPublic = false)
public class UserReportDetailController extends BaseController {
    private UserReportRepository userReportRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.userReportRepository = new UserReportRepository();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/report/user_report-detail.jsp");

        String rid = req.getParameter("id");

        try {
            if (StringValidator.isNullOrBlank(rid) || !StringValidator.isUUID(rid)) {
                req.setAttribute("ERROR_MESSAGE", "Id không hợp lệ!");
                dispatcher.forward(req, resp);
            }

            UUID id = UUID.fromString(rid);
            Optional<UserReportEntity> optionalUserReport = userReportRepository
                    .getReportById(id);

            if (optionalUserReport.isEmpty()) {
                req.setAttribute("ERROR_MESSAGE", "Không tìm thấy báo cáo!");
                dispatcher.forward(req, resp);
                return;
            }

            UserReportEntity userReportEntity = optionalUserReport.get();
            req.setAttribute("VAR_REPORT_DETAIL", userReportEntity);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("ERROR_MESSAGE", "Không tìm thấy báo cáo!");
        }

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();

        String reqId = req.getParameter("id");
        try {
            if (StringValidator.isNullOrBlank(reqId) || !StringValidator.isUUID(reqId)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Tham số không hợp lệ!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            UUID rid = UUID.fromString(reqId);
            Optional<UserReportEntity> optionalUserReport = userReportRepository
                    .getReportById(rid);

            if (optionalUserReport.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy báo cáo!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            UserReportEntity userReportEntity = optionalUserReport.get();
            userReportEntity.setStatus(ReportConstant.REPORT_ABORT);

            boolean isSuccess = userReportRepository.update(userReportEntity);
            if(isSuccess){
                resp.setStatus(200);
                jsonObject.addProperty("code", 200);
                jsonObject.addProperty("message", "Cập nhật thành công!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            resp.setStatus(409);
            jsonObject.addProperty("code", 409);
            jsonObject.addProperty("message", "Cập nhật không thành công!");
            resp.getWriter().write(gson.toJson(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            jsonObject.addProperty("code", 500);
            jsonObject.addProperty("message", "Lỗi server!");
            resp.getWriter().write(gson.toJson(jsonObject));
        }
    }
}
