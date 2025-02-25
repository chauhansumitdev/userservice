package com.example.userservice.retrofit;

import com.example.userservice.dto.OrderRequest;
import com.example.userservice.dto.OrderStatus;
import com.example.userservice.dto.RequestAuth;
import com.example.userservice.dto.ResponseAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.UUID;


@Slf4j
@Component
public class APIResolver {

    private final DiscoveryClient discoveryClient;
    private final OrderInterface orderInterface;
    private final AuthInterface authInterface;

    public APIResolver(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        this.authInterface = createRetrofit("AUTH-SERVICE").create(AuthInterface.class);
        this.orderInterface = createRetrofit("ORDER-SERVICE").create(OrderInterface.class);
    }


    private Retrofit createRetrofit(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

        log.info(instances.get(0).toString());

        if (instances.isEmpty()) {
            throw new IllegalStateException("NO INSTANCE AVAILABLE FOR " + serviceName);
        }

        String baseUrl = instances.get(0).getUri().toString();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }




    public void updateOrder(OrderStatus orderStatus, UUID id) throws Exception{
        Call<Void> updateOrder = orderInterface.updateOrder(orderStatus, id);
        Response<Void> response = updateOrder.execute();

        if(response.isSuccessful()){
            log.info("STATUS UPDATED");
            return;
        }

        throw new Exception("CANNOT UPDATE STATUS");
    }



    public UUID createOrder(OrderRequest orderRequest, UUID id) throws Exception{

        Call<UUID> order = orderInterface.createOrder(orderRequest, id);

        Response<UUID> response = order.execute();

        if(response.isSuccessful()){
            return response.body();
        }

        throw new Exception("ERROR PLACING ORDER");
    }




    public ResponseAuth verifyToken(String token) throws Exception {
        Call<ResponseAuth> caller = authInterface.verify(token);
        Response<ResponseAuth> response = caller.execute();

        if (response.isSuccessful()) {
            return response.body();
        }

        throw new Exception("INVALID TOKEN");
    }





    public ResponseAuth generateToken(RequestAuth requestAuth) throws Exception {
        Call<ResponseAuth> caller = authInterface.generate(requestAuth);
        Response<ResponseAuth> response = caller.execute();

        if (response.isSuccessful()) {
            return response.body();
        }

        throw new Exception("INVALID TOKEN");
    }


}
