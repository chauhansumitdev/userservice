package com.example.userservice.mapper;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public User toDAO(UserDTO userDTO){

        User user = new User();
        user.setRole(userDTO.getRole());
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setProfile(userDTO.getProfile());

        return user;
    }

    public UserDTO toDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(user.getUserName());
        userDTO.setRole(user.getRole());
        userDTO.setId(user.getId());
        userDTO.setProfile(user.getProfile());

        return userDTO;
    }
}
