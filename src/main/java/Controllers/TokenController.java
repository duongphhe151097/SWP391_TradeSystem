package Controllers;

import DataAccess.TokenActivationRepository;
import Models.TokenActivationEntity;
import Models.UserEntity;
import Utils.Annotations.Authorization;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TokenController", urlPatterns = {"/TokenController"})
@Authorization(role = "", isPublic = true)
public class TokenController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token != null && !token.isEmpty()) {
            TokenActivationRepository tokenRepository = new TokenActivationRepository();
            Optional<TokenActivationEntity> tokenEntityOptional = tokenRepository.getTokenByToken(token);

            if (tokenEntityOptional.isPresent()){
                TokenActivationEntity tokenEntity = tokenEntityOptional.get();

                if (!tokenEntity.isUsed() && tokenEntity.getExpriedAt().isAfter(LocalDateTime.now())){
                    // Activate the user (change status from 0 to 1)
                    UserEntity userEntity = getUserById(tokenEntity.getUserId());

                    if (userEntity != null) {
                        userEntity.setStatus((short) 1);
                        updateUser(userEntity);

                        // Mark the token as used
                        tokenEntity.setUsed(true);
                        tokenRepository.updateToken(tokenEntity);

                        // Redirect to a success page
                        response.sendRedirect(request.getContextPath() + "/activation-success.jsp");
                        return;
                    }
                }
            }
        }

        // Redirect to an error page if activation fails
        response.sendRedirect(request.getContextPath() + "/error.jsp");
    }

    private void updateUserStatus(String token) {
        TokenActivationEntity activationEntity = retrieveActivationEntity(token);

        if (activationEntity != null && !activationEntity.isUsed()) {
            // Activate the user (set status to 1)
            UserEntity user = retrieveUserById(activationEntity.getUserId());
            if (user != null) {
                user.setStatus(ActivationType.ACTIVE_REQUEST);
                // Save the updated user entity
                saveUser(user);

                // Mark the activation token as used
                activationEntity.setUsed(true);
                saveActivationEntity(activationEntity);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }



}
