package Controllers;

import DataAccess.TokenActivationRepository;
import DataAccess.UserRepository;
import Models.TokenActivationEntity;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import Utils.Constants.ActivationType;
import Utils.Constants.UserConstant;
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
        String token = request.getParameter("token");

        if (token != null && !token.isEmpty()) {
            try {
                TokenActivationRepository tokenRepository = new TokenActivationRepository();
                Optional<TokenActivationEntity> tokenEntityOptional = tokenRepository.getTokenByToken(token);

                if (tokenEntityOptional.isPresent()) {
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

                            request.setAttribute("activationMessage", "Active tai khoan thanh cong!");
                            // Redirect to a success page
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/ActiveAccount.jsp");
                            dispatcher.forward(request, response);
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Active tai khoan that bai!");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error.jsp");
                dispatcher.forward(request, response);
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
