package lk.ijse.orderservice.services.impl;

import lk.ijse.orderservice.dto.OrderDTO;

import lk.ijse.orderservice.entity.Customers;
import lk.ijse.orderservice.entity.Orders;
import lk.ijse.orderservice.repo.CustomerRepo;
import lk.ijse.orderservice.repo.OrderRepo;
import lk.ijse.orderservice.services.OrderService;
import lk.ijse.orderservice.util.VarList;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepository;
    private final CustomerRepo customerProfileRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepo orderRepository,
                            CustomerRepo customerProfileRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public int createOrder(OrderDTO orderDTO) {
        try {
            if (orderDTO.getId() != null && orderRepository.existsById(orderDTO.getId())) {
                return VarList.Not_Acceptable;
            }

            Customers customer = customerProfileRepository.findById(orderDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            Orders order = modelMapper.map(orderDTO, Orders.class);
            order.setCustomerProfile(customer);

            orderRepository.save(order);
            return VarList.Created;

        } catch (Exception e) {
            logger.error("Error saving order", e);
            return VarList.Internal_Server_Error;
        }
    }


    @Override
    public int updateOrder(OrderDTO orderDTO) {
        try {
            if (orderDTO.getId() == null || !orderRepository.existsById(orderDTO.getId())) {
                return VarList.Not_Found;
            }

            Orders order = orderRepository.findById(orderDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            Customers customer = customerProfileRepository.findById(orderDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            order.setCustomerProfile(customer);
            order.setReceiverName(orderDTO.getReceiverName());
            order.setReceiverAddress(orderDTO.getReceiverAddress());
            order.setReceiverPhone(orderDTO.getReceiverPhone());
            order.setWeight(orderDTO.getWeight());
            order.setStatus(orderDTO.getStatus());
            order.setCreatedBy(orderDTO.getCreatedBy());

            orderRepository.save(order);
            return VarList.OK;

        } catch (Exception e) {
            logger.error("Error updating order", e);
            return VarList.Internal_Server_Error;
        }
    }


    @Override
    public List<OrderDTO> getAllOrders() {
        try {
            List<Orders> orders = orderRepository.findAll();
            return orders.stream()
                    .map(order -> modelMapper.map(order, OrderDTO.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error fetching orders", e);
            return List.of();
        }
    }

    @Override
    public int deleteOrder(Long id) {
        return 0;
    }

    @Override
    public int getOrderById(Long id) {
        return 0;
    }



}