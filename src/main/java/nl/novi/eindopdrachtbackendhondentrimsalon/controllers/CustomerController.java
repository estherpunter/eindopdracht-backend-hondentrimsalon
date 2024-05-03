package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    private CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }


    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Customer> addCustomer(@RequestParam String customerName,
                                                @RequestParam String phoneNumber) {
        Customer newCustomer = customerService.addCustomer(customerName, phoneNumber);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }


    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long customerId,
                                                   @RequestBody Customer updatedCustomer) {
        Customer customer = customerService.updateCustomer(customerId, updatedCustomer);
        return ResponseEntity.ok(customer);
    }


    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{customerId}/dogs")
    public ResponseEntity<Customer> addDogToCustomer(@PathVariable Long customerId,
                                                     @RequestParam String dogName,
                                                     @RequestParam String breed,
                                                     @RequestParam int age) {
        Customer updatedCustomer = customerService.addDogToCustomer(customerId, dogName, breed, age);
        return ResponseEntity.ok(updatedCustomer);
    }


    @PutMapping("/{customerId}/dogs/{dogId}")
    public ResponseEntity<Customer> updateDogForCustomer(@PathVariable Long customerId,
                                                         @PathVariable Long dogId,
                                                         @RequestParam String dogName,
                                                         @RequestParam String breed,
                                                         @RequestParam int age) {
        try {
            customerService.updateDogForCustomer(customerId, dogId, dogName, breed, age);
            return ResponseEntity.ok().build();
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{customerId}/dogs/{dogId}")
    public ResponseEntity<Void> removeDogFromCustomer(@PathVariable Long customerId,
                                                      @PathVariable Long dogId) {
        try {
            customerService.removeDogFromCustomer(customerId, dogId);
            return ResponseEntity.noContent().build();
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }


}
