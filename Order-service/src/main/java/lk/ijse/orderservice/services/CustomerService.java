package lk.ijse.orderservice.services;

import lk.ijse.orderservice.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    int saveCustomer(CustomerDTO customer);
    int updateCustomer(CustomerDTO customer);
    int deleteCustomer(CustomerDTO customer);
    List<CustomerDTO> getAll() ;
}
