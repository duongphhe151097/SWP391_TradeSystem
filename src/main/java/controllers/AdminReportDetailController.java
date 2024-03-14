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

@WebServlet(name = "AdminReportDetailController", urlPatterns = "/admin/report/detail")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminReportDetailController extends BaseController {
    private UserReportRepository userReportRepository;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.userReportRepository = new UserReportRepository();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/admin/admin_report-detail.jsp");
        try {

            String reqId = req.getParameter("id");

            if (StringValidator.isNullOrBlank(reqId) || !StringValidator.isUUID(reqId)) {
                req.setAttribute("ERROR_MESSAGE", "Id báo cáo không hợp lệ!");
                dispatcher.forward(req, resp);
                return;
            }

            UUID id = UUID.fromString(reqId);
            Optional<UserReportEntity> optionalUserReport = userReportRepository
                    .getReportById(id);

            if (optionalUserReport.isEmpty()) {
                req.setAttribute("ERROR_MESSAGE", "Không tìm thấy báo cáo!");
                dispatcher.forward(req, resp);
                return;
            }

            req.setAttribute("VAR_DATA", optionalUserReport.get());
        } catch (Exception e) {
            req.setAttribute("ERROR_MESSAGE", "Xảy ra lỗi khi load dữ liệu");
        }
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        JsonObject jsonObject = new JsonObject();

        String reqId = req.getParameter("id");
        String reqType = req.getParameter("type");
        String reqAdminResponse = req.getParameter("adm_res");
        String reqIsRightReport = req.getParameter("right_report");

        try {
            if (StringValidator.isNullOrBlank(reqId) || !StringValidator.isUUID(reqId) || StringValidator.isNullOrBlank(reqType)) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Tham số không hợp lệ!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            UUID id = UUID.fromString(reqId);
            Optional<UserReportEntity> optionalUserReport = userReportRepository
                    .getReportById(id);

            if (optionalUserReport.isEmpty()) {
                resp.setStatus(400);
                jsonObject.addProperty("code", 400);
                jsonObject.addProperty("message", "Không tìm thấy báo cáo!");
                resp.getWriter().write(gson.toJson(jsonObject));
                return;
            }

            UserReportEntity userReportEntity = optionalUserReport.get();
            switch (reqType) {
                case "PROCESSING":
                    userReportEntity.setStatus(ReportConstant.REPORT_PROCESSING);
                    break;

                case "PROCESSED":
                    if (StringValidator.isNullOrBlank(reqAdminResponse)) {
                        resp.setStatus(400);
                        jsonObject.addProperty("code", 400);
                        jsonObject.addProperty("message", "Không được để trống phản hồi");
                        resp.getWriter().write(gson.toJson(jsonObject));
                        return;
                    }

                    //Report đúng
                    userReportEntity.setStatus(ReportConstant.REPORT_DONE_CLIENT_RIGHT);
                    if(!StringValidator.isNullOrBlank(reqIsRightReport) && reqIsRightReport.equals("checked")){
                        //Report sai
                        userReportEntity.setStatus(ReportConstant.REPORT_DONE_CLIENT_WRONG);
                    }
                    userReportEntity.setAdminResponse(reqAdminResponse.replace("&nbsp;",""));
                    break;
            }

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
