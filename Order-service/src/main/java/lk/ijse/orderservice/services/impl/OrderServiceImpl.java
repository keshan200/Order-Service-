package lk.ijse.orderservice.services.impl;

import lk.ijse.orderservice.dto.OrderDTO;

import lk.ijse.orderservice.dto.TrackingRequest;
import lk.ijse.orderservice.entity.Customers;
import lk.ijse.orderservice.entity.Orders;
import lk.ijse.orderservice.enums.OrderStatus;
import lk.ijse.orderservice.proxy.DocumentClient;
import lk.ijse.orderservice.proxy.TrackingClient;
import lk.ijse.orderservice.repo.CustomerRepo;
import lk.ijse.orderservice.repo.OrderRepo;
import lk.ijse.orderservice.services.OrderService;
import lk.ijse.orderservice.util.VarList;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepository;
    private final CustomerRepo customerProfileRepository;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final TrackingClient trackingClient;

    public OrderServiceImpl(OrderRepo orderRepository,
                            CustomerRepo customerProfileRepository,
                            ModelMapper modelMapper,
                            TrackingClient trackingClient) {
        this.orderRepository = orderRepository;
        this.customerProfileRepository = customerProfileRepository;
        this.modelMapper = modelMapper;
        this.trackingClient = trackingClient;
    }


    @Autowired
    private DocumentClient documentClient;

    @Override
    public Object getFullOrder(String orderCode) {
        try {

            Orders order = orderRepository.findByOrderCode(orderCode);

            if (order == null) {
                return VarList.Not_Found;
            }





            Map<String, Object> response = new HashMap<>();

            response.put("order", order);


            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return VarList.Internal_Server_Error;
        }
    }

    @Override
    public String createOrder(OrderDTO orderDTO) {
        try {
            // 1. Check existence
            if (orderDTO.getId() != null && orderRepository.existsById(orderDTO.getId())) {
                return "ALREADY_EXISTS";
            }

            // 2. Find Customer
            Customers customer = customerProfileRepository.findById(orderDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // 3. Generate Order Code
            String orderCode = "ORD-" + System.currentTimeMillis();

            // 4. Map and Set Data
            Orders order = modelMapper.map(orderDTO, Orders.class);
            order.setOrderCode(orderCode);
            order.setCustomerProfile(customer);
            order.setStatus(OrderStatus.PENDING); // Enum set කිරීම

            // 5. Save to MySQL
            orderRepository.save(order);

            // 6. Prepare Tracking Request
            TrackingRequest trackReq = new TrackingRequest();
            trackReq.setOrderCode(order.getOrderCode());
            trackReq.setStatus(OrderStatus.PENDING.name());
            trackReq.setUpdatedBy(orderDTO.getCreatedBy());

            trackingClient.initializeTracking(trackReq);


            // 7. Call Tracking Service
            trackingClient.initializeTracking(trackReq);


            System.out.println("Order Code: " + orderCode);
            System.out.println("User ID: " + orderDTO.getCreatedBy());

            return orderCode;

        } catch (Exception e) {

            logger.error("Error saving order or calling tracking service", e);
            return "ERROR";
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