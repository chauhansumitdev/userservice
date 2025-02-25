package com.example.userservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductDetails {
    UUID id;
    Integer quantity;
}
