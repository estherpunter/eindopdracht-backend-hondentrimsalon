package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long customerId, Customer customer) {
        //Check if the customer with the given ID exists
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        //Update customer properties
        existingCustomer.setName(customer.getName());
        existingCustomer.setPhonenumber(customer.getPhonenumber());

        //Save the updated customer
        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long customerId) {
        //Check if the customer with the given ID exists
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cusotmer not found with this id: " + customerId));

        //Delete the customer
        customerRepository.delete(existingCustomer);
    }

    public Customer getCustomerById(Long customerId) {
        //Retrieve customer by ID
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id" + customerId));
    }


    public List<Customer> getAllCustomers() {
        // Retrieve all customers
        return customerRepository.findAll();
    }
}
