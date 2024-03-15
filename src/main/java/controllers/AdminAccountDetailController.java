package controllers;


import dataAccess.RoleRepository;
import dataAccess.UserRepository;
import models.RoleEntity;
import models.UserEntity;
import utils.annotations.Authorization;
import utils.validation.StringValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@WebServlet(name = "AdminAccountDetailController", urlPatterns = "/admin/account/detail")
@Authorization(role = "ADMIN", isPublic = false)
public class AdminAccountDetailController extends BaseController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userRepository = new UserRepository();
        this.roleRepository = new RoleRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userIdRequest = req.getParameter("id");
        String userNameRequest = req.getParameter("uname");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/pages/admin/admin_account-detail.jsp");

        if (StringValidator.isNullOrBlank(userIdRequest) && StringValidator.isNullOrBlank(userNameRequest)) {
            req.setAttribute("ERROR_NOTFOUND", true);
            dispatcher.forward(req, resp);
            return;
        }

        Optional<UserEntity> optionalUserEntity = isUserOrId(userIdRequest, userNameRequest);

        if (optionalUserEntity.isEmpty()) {
            req.setAttribute("ERROR_NOTFOUND", true);
            dispatcher.forward(req, resp);
            return;
        }

        Optional<List<RoleEntity>> roleEntities = roleRepository.getAllRole();
        if (roleEntities.isEmpty()) {
            req.setAttribute("ERROR_NOTFOUND", true);
            dispatcher.forward(req, resp);
            return;
        }

        UserEntity userEntity = optionalUserEntity.get();
        Optional<Set<RoleEntity>> userRole = roleRepository
                .getRoleByUserId(userEntity.getId());

        if (userRole.isEmpty()) {
            req.setAttribute("ERROR_NOTFOUND", true);
            dispatcher.forward(req, resp);
            return;
        }

        req.setAttribute("VAR_USERID", userEntity.getId());
        req.setAttribute("VAR_USERNAME", userEntity.getUsername());
        req.setAttribute("VAR_EMAIL", userEntity.getEmail());
        req.setAttribute("VAR_FULLNAME", userEntity.getFullName());
        req.setAttribute("VAR_PHONE", userEntity.getPhoneNumber());
        req.setAttribute("VAR_ADDRESS", userEntity.getAddress());
        req.setAttribute("VAR_STATUS", userEntity.getStatus());
        req.setAttribute("VAR_BALANCE", userEntity.getBalance());
        req.setAttribute("VAR_CREATEDATE", userEntity.getCreateAt());
        req.setAttribute("VAR_RATE", userEntity.getRating());

        req.setAttribute("ROLES", roleEntities.get());
        req.setAttribute("USER_ROLES", userRole.get());

        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    private Optional<UserEntity> isUserOrId(String uid, String uname) {
        if (!StringValidator.isNullOrBlank(uid)) {
            return userRepository
                    .getUserById(UUID.fromString(uid));
        }

        if (!StringValidator.isNullOrBlank(uname)) {
            return userRepository
                    .getUserByUsername(uname);
        }

        return Optional.empty();
    }
}
