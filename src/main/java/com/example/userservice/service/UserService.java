package com.example.userservice.service;


import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User registerUser(User user){
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User updatedUser) {
        log.info("USER UPDATED");
        User existingUser = getUser(id);
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setProfile(updatedUser.getProfile());
        return userRepository.save(existingUser);
    }

    public User getUser(UUID id){
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            return user.get();
        }else{
            return null;
        }
    }

    public void deleteUser(UUID id){
        userRepository.deleteById(id);
    }

//    public String login(User user){
//
//    }
}
