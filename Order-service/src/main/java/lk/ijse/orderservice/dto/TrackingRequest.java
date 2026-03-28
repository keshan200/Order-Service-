package lk.ijse.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingRequest {
    private String orderCode;
    private String status;
    private int updatedBy;
}