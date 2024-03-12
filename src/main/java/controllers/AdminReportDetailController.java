package controllers;

import jakarta.servlet.annotation.WebServlet;
import utils.annotations.Authorization;

@WebServlet(name = "AdminReportController", urlPatterns = "/admin/report/detail")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminReportDetailController extends BaseController{
}
