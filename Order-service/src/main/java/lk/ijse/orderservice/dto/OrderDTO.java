package lk.ijse.orderservice.dto;

import lk.ijse.orderservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderCode;
    private Long customerId;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private Double weight;
    private OrderStatus status;
    private Long createdBy;
}