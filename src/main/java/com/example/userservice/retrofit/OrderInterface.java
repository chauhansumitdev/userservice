package com.example.userservice.retrofit;

import com.example.userservice.dto.OrderRequest;
import com.example.userservice.dto.OrderStatus;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.UUID;

public interface OrderInterface {

    @POST("/api/v1/order/{id}")
    Call<UUID> createOrder(@Body OrderRequest orderRequest, @Path("id") UUID id);

    @PUT("/api/v1/order/{id}")
    Call<Void> updateOrder(@Body OrderStatus orderStatus, @Path("id") UUID id);

}
