package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserContoller {

    private final UserService userService;



    public UserContoller(UserService userService) {
        this.userService = userService;
    }



    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user) throws Exception{
        return ResponseEntity.ok(userService.registerUser(user));
    }





    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader)  throws  Exception{

        log.info("REQUESTING USER DATA");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return new ResponseEntity<>(userService.getUser(id, authHeader), HttpStatus.OK);
    }




    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id,@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,@Valid @RequestBody User user)  throws Exception{
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userService.updateUser(id, authHeader, user));
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader)  throws Exception{
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userService.deleteUser(id, authHeader);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/login")
    public ResponseEntity<ResponseAuth> login(@RequestBody RequestAuth requestAuth) throws  Exception{
        return new ResponseEntity<>(userService.login(requestAuth), HttpStatus.OK);
    }


    @PostMapping("/order/{id}")
    public ResponseEntity<UUID> createOrder(@RequestBody OrderRequest orderRequest,@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @PathVariable UUID id) throws Exception{
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("CREATING ORDER");
        return new ResponseEntity<>(userService.createOrder(orderRequest, authHeader, id), HttpStatus.OK);
    }


    @PutMapping("/order/{orderid}")
    public ResponseEntity<Void> updateOrder(
            @PathVariable UUID orderid,
            @RequestBody OrderStatus orderStatus,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) throws Exception {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.updateOrder(orderid, orderStatus, authHeader);

        return ResponseEntity.ok().build();
    }

}
