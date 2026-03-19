package lk.ijse.orderservice.services.impl;

import lk.ijse.orderservice.dto.CustomerDTO;
import lk.ijse.orderservice.entity.Customers;
import lk.ijse.orderservice.repo.CustomerRepo;
import lk.ijse.orderservice.services.CustomerService;
import lk.ijse.orderservice.util.VarList;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepo customerRepository;


    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Override
    public int saveCustomer(CustomerDTO customerDTO) {
        try {
            // Duplicate check
            if (customerDTO.getId() != null && customerRepository.existsById(customerDTO.getId())) {
                return VarList.Not_Acceptable;
            }

            // Convert DTO -> Entity
            Customers customer = modelMapper.map(customerDTO, Customers.class);

            customerRepository.save(customer);

            return VarList.Created;

        } catch (IllegalArgumentException e) {
            logger.error("Validation Error: {}", e.getMessage());
            return VarList.Not_Acceptable;

        } catch (Exception e) {
            logger.error("Error saving customer", e);
            return VarList.Internal_Server_Error;
        }
    }

    @Override
    public int updateCustomer(CustomerDTO customerDTO) {
        try {
            if (customerDTO.getId() == null || !customerRepository.existsById(customerDTO.getId())) {
                return VarList.Not_Found;
            }

            Customers customer = modelMapper.map(customerDTO, Customers.class);

            customerRepository.save(customer);

            return VarList.OK;

        } catch (Exception e) {
            logger.error("Error updating customer", e);
            return VarList.Internal_Server_Error;
        }
    }

    @Override
    public int deleteCustomer(CustomerDTO customerDTO) {
        try {
            if (customerDTO.getId() == null || !customerRepository.existsById(customerDTO.getId())) {
                return VarList.Not_Found;
            }

            customerRepository.deleteById(customerDTO.getId());

            return VarList.OK;

        } catch (Exception e) {
            logger.error("Error deleting customer", e);
            return VarList.Internal_Server_Error;
        }
    }

    @Override
    public List<CustomerDTO> getAll() {
        try {
            List<Customers> customerList = customerRepository.findAll();

            return customerList.stream()
                    .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error fetching customers", e);
            return List.of();
        }
    }
}