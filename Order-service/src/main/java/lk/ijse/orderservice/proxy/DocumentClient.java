package lk.ijse.orderservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "document-service", url = "http://localhost:8083")
public interface DocumentClient {

    @GetMapping("/proof/{orderCode}")
    List<Object> getProof(@PathVariable String orderCode);
}