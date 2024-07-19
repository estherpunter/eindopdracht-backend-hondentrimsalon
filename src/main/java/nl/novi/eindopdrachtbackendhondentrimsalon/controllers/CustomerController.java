package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
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

    @GetMapping("/allcustomers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long customerId) {
        CustomerDto customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }


    @PostMapping("/allcustomers")
    public ResponseEntity<CustomerDto> addCustomer(@RequestParam String customerName,
                                                   @RequestParam String phoneNumber) {
        CustomerDto newCustomer = customerService.addCustomer(customerName, phoneNumber);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }


    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long customerId,
                                                      @RequestBody CustomerDto updatedCustomer) {
        CustomerDto customer = customerService.updateCustomer(customerId, updatedCustomer);
        return ResponseEntity.ok(customer);
    }


    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{customerId}/dogs")
    public ResponseEntity<CustomerDto> addDogToCustomer(@PathVariable Long customerId,
                                                        @RequestParam String dogName,
                                                        @RequestParam String breed,
                                                        @RequestParam int age) {
        CustomerDto updatedCustomer = customerService.addDogToCustomer(customerId, dogName, breed, age);
        return ResponseEntity.ok(updatedCustomer);
    }


    @PutMapping("/{customerId}/dogs/{dogId}")
    public ResponseEntity<Void> updateDogForCustomer(@PathVariable Long customerId,
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
