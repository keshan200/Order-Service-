package lk.ijse.orderservice.repo;


import lk.ijse.orderservice.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Orders, Long> {

    Orders findByOrderCode(String orderCode);
}