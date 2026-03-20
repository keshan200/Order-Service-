package lk.ijse.orderservice.services;


import lk.ijse.orderservice.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    int createOrder(OrderDTO orderDTO);
    int updateOrder(OrderDTO orderDTO);
    int getOrderById(Long id);
    List<OrderDTO> getAllOrders();
    int deleteOrder(Long id);
}