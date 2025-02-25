package com.example.userservice.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ResponseAuth {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jwt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role;

}