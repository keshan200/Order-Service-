package lk.ijse.orderservice.controller;

import jakarta.validation.Valid;
import lk.ijse.orderservice.dto.OrderDTO;
import lk.ijse.orderservice.dto.ResponseDTO;
import lk.ijse.orderservice.services.OrderService;
import lk.ijse.orderservice.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @GetMapping("/full/{orderCode}")
    public ResponseEntity<ResponseDTO> getFullOrder(
            @PathVariable String orderCode) {

        Object data = orderService.getFullOrder(orderCode);

        if (data instanceof Integer && (int) data == VarList.Not_Found) {
            return ResponseEntity.status(404).body(
                    new ResponseDTO(VarList.Not_Found, "Not Found", null)
            );
        } else if (data instanceof Integer && (int) data == VarList.Internal_Server_Error) {
            return ResponseEntity.status(500).body(
                    new ResponseDTO(VarList.Internal_Server_Error, "Error", null)
            );
        } else {
            return ResponseEntity.ok(
                    new ResponseDTO(VarList.OK, "Success", data)
            );
        }
    }


    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        try {
            String result = orderService.createOrder(orderDTO);

            if (result.startsWith("ORD-")) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Order created successfully. Code: " + result, result));
            }

            switch (result) {
                case "ALREADY_EXISTS" -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Order already exists", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO(VarList.Bad_Request, "Error saving order", null));
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Internal server error", e.getMessage()));
        }
    }


    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> updateOrder(@RequestBody @Valid OrderDTO orderDTO) {
        try {
            int result = orderService.updateOrder(orderDTO);

            switch (result) {
                case VarList.OK -> {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.OK, "Order updated successfully", orderDTO));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Order not found", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO(VarList.Bad_Request, "Update failed", null));
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Internal Server Error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteOrder(@PathVariable Long id) {
        try {
            int result = orderService.deleteOrder(id);

            switch (result) {
                case VarList.OK -> {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.OK, "Order deleted successfully", id));
                }
                case VarList.Not_Found -> {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Order not found", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO(VarList.Bad_Request, "Delete failed", null));
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Internal Server Error", e.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            List<OrderDTO> list = orderService.getAllOrders();

            if (list.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseDTO(VarList.No_Content, "No order found", null));
    }
}