package lk.ijse.orderservice.repo;

import lk.ijse.orderservice.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customers, Long> {
}
