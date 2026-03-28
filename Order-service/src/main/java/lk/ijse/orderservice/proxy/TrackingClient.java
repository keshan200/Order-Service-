package lk.ijse.orderservice.proxy;

import feign.Headers;
import lk.ijse.orderservice.config.FeignConfig;
import lk.ijse.orderservice.dto.TrackingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "tracking-service", url = "http://localhost:8082" , configuration = FeignConfig.class)
public interface TrackingClient {

    @PostMapping(
            value = "/api/v1/tracking/init",
            consumes = "application/json",
            produces = "application/json"
    )
    void initializeTracking(@RequestBody TrackingRequest request);
}