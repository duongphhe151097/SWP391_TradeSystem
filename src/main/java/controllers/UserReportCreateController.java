package controllers;

import jakarta.servlet.annotation.WebServlet;
import utils.annotations.Authorization;

@WebServlet(name = "UserReportCreateController", urlPatterns = "/create")
@Authorization(role = "USER", isPublic = false)
public class UserReportCreateController extends BaseController {

}
