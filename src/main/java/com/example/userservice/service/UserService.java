package com.example.userservice.service;


import com.example.userservice.dto.*;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.retrofit.APIResolver;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {


    private final UserRepository userRepository;
    private final APIResolver authResolver;
    private final UserMapper userMapper;



    @Autowired
    public UserService(UserMapper userMapper, APIResolver authResolver, UserRepository userRepository){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authResolver = authResolver;
    }




    public UserDTO registerUser(UserDTO user) throws  Exception{
        if(user.getRole().toLowerCase().equals("admin")){
            user.setRole("ADMIN");
        }else{
            user.setRole("USER");
        }


        User userdao = userMapper.toDAO(user);
        userdao.setPassword(hashPassword(user.getPassword()));

        User usersaved = null;

        try{
            usersaved =userRepository.save(userdao);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

        return userMapper.toDTO(usersaved);
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // 12 is the work factor
    }





//    public User updateUser(UUID id,String token, User updatedUser) throws Exception{
//        User existingUser = getUser(id);
//
//        existingUser.setUserName(updatedUser.getUserName());
//        existingUser.setPassword(updatedUser.getPassword());
//        existingUser.setProfile(updatedUser.getProfile());
//        return userRepository.save(existingUser);
//    }

    public User updateUser(UUID id,String token, User updatedUser) throws Exception{
        User existingUser = getUser(id, token);

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setProfile(updatedUser.getProfile());
        return userRepository.save(existingUser);
    }





//    public User getUser(UUID id) throws Exception{
//
//        //ResponseAuth responseAuth = authResolver.verify(token.substring(7));
//
//        Optional<User> user = userRepository.findById(id);
//
//       if(!user.isPresent()){
//           throw new Exception("USER DOES NOT EXIST");
//       }
//
//       User existingUser = user.get();
//
//      // if(responseAuth.getUsername().equals(existingUser.getUserName()) || responseAuth.getRole().equals("ADMIN")){
//           return existingUser;
//       //}
//
//       // throw new Exception("ACCESS NOT GRANTED");
//    }

    public User getUser(UUID id, String token) throws Exception{

        ResponseAuth responseAuth = authResolver.verifyToken(token.substring(7));

        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new Exception("USER DOES NOT EXIST");
        }

        User existingUser = user.get();

        if(responseAuth.getUsername().equals(existingUser.getUserName()) || responseAuth.getRole().equals("ADMIN")){
            return existingUser;
        }

        throw new Exception("ACCESS NOT GRANTED");
    }


    public UUID createOrder(OrderRequest orderRequest, String token, UUID id) throws Exception{
        User user = getUser(id, token);

        return authResolver.createOrder(orderRequest, id);

    }



    public void deleteUser(UUID id, String token) throws Exception{

        ResponseAuth responseAuth = authResolver.verifyToken(token.substring(7));

        Optional<User> existingUser = userRepository.findById(id);

        if(!existingUser.isPresent()){
            throw new Exception("USER DOES NOT EXIST");
        }

        User user = existingUser.get();

        if(responseAuth.getUsername().equals(user.getUserName())){
            userRepository.deleteById(id);
            return;
        }

        throw new Exception("INVALID TOKEN/ID");
    }



    public ResponseAuth login(RequestAuth requestAuth) throws  Exception{

        String username = requestAuth.getUsername();
        String password = requestAuth.getPassword();

        Optional<User> findUser = userRepository.findByUserName(username);

        if(findUser.isPresent() && verifyPassword(password,findUser.get().getPassword())){

            requestAuth.setRole(findUser.get().getRole());
            return authResolver.generateToken(requestAuth);

        }

        throw new Exception("INVALID CREDENTIALS");

    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }



    public void updateOrder(UUID orderid, OrderStatus orderStatus, String token) throws  Exception{

        ResponseAuth user = authResolver.verifyToken(token.substring(7));

        if(user.getRole().equals("ADMIN") && !orderStatus.getStatus().equals("CANCELLED")){

            String status = orderStatus.getStatus();

            authResolver.updateOrder(orderStatus, orderid);


        }else if(user.getRole().equals("USER")){

            if(orderStatus.getStatus().equals("CANCELLED")){
                authResolver.updateOrder(orderStatus, orderid);
            }

            throw new Exception("CANNOT UPDATE ORDER");
        }else{
            throw new Exception("CANNOT UPDATE ORDER");
        }

    }
}
