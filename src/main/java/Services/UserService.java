/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import DataAccess.UserRepository;

import java.util.UUID;

/**
 *
 * @author toden
 */
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(){
        userRepository = new UserRepository();
    }
    
    public boolean UpdateUserStatus(UUID userId, short status){
        return userRepository.updateUserStatus(userId, status);
    }
}
