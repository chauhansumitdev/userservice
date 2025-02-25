package com.example.userservice.dto;


import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    List<ProductDetails> products;
}
