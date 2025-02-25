package com.example.userservice.dto;

import com.example.userservice.entity.Profile;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID id;

    @NotBlank
    private String userName;

    @NotBlank
    private String role;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @NotNull
    private Profile profile;
}
