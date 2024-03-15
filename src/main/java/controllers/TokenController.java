package controllers;

import dataAccess.TokenActivationRepository;
import dataAccess.UserRepository;
import models.TokenActivationEntity;
import models.UserEntity;
import utils.annotations.Authorization;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import utils.constants.UserConstant;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TokenController", urlPatterns = {"/active"})
@Authorization(role = "", isPublic = true)
public class TokenController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("t");

        RequestDispatcher dispatcher = request.getRequestDispatcher("/pages/active_account.jsp");
        try {
            if (token == null && token.isEmpty()) {
                request.setAttribute("FAILED_MESSAGE", "Mã kích hoạt không hợp lệ!");
                dispatcher.forward(request, response);
                return;
            }

            TokenActivationRepository tokenRepository = new TokenActivationRepository();
            Optional<TokenActivationEntity> tokenEntityOptional = tokenRepository.getTokenByToken(token);

            if (tokenEntityOptional.isEmpty()) {
                request.setAttribute("FAILED_MESSAGE", "Mã kích hoạt không hợp lệ!");
                dispatcher.forward(request, response);
                return;
            }
            TokenActivationEntity tokenEntity = tokenEntityOptional.get();

            if (!tokenEntity.isUsed() && tokenEntity.getExpriedAt().isAfter(LocalDateTime.now())) {
                // Activate the user (change status from 0 to 1)
                UserRepository userRepository = new UserRepository();
                Optional<UserEntity> userEntityOptional = userRepository.getUserById(tokenEntity.getUserId());

                if (userEntityOptional.isPresent()) {
                    UserEntity userEntity = userEntityOptional.get();

                    // Update user status
                    userRepository.updateUserStatus(userEntity.getId(), UserConstant.ACTIVE);

                    // Mark the token as used
                    tokenEntity.setUsed(true);
                    tokenRepository.updateToken(tokenEntity);

                    request.setAttribute("SUCCESS_MESSAGE", "Kích hoạt tài khoản thành công!");
                    // Redirect to a success page
                    dispatcher.forward(request, response);
                    return;
                }
            }

            request.setAttribute("FAILED_MESSAGE", "Mã kích hoạt không hợp lệ!");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            request.setAttribute("FAILED_MESSAGE", "Active tai khoan that bai!");
            dispatcher.forward(request, response);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
