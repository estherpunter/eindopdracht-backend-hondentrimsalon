package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.DogDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.CustomerNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.DogNotFoundException;
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
        List<CustomerDto> customerDtos = customerService.getAllCustomers();
        return ResponseEntity.ok(customerDtos);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long customerId) {
        CustomerDto customerDto = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customerDto);
    }


    @PostMapping("")
    public ResponseEntity<CustomerDto> addCustomer(@RequestParam("customerName") String customerName,
                                                   @RequestParam("phoneNumber") String phoneNumber) {
        CustomerDto newCustomerDto = customerService.addCustomer(customerName, phoneNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCustomerDto);
    }


    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long customerId,
                                                      @RequestParam("customerName") String customerName,
                                                      @RequestParam("phoneNumber") String phoneNumber) {
        CustomerDto updatedCustomer = customerService.updateCustomer(customerId, customerName, phoneNumber);
        return ResponseEntity.ok(updatedCustomer);
    }


    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{customerId}/dogs")
    public ResponseEntity<CustomerDto> addDogToCustomer(@PathVariable Long customerId,
                                                        @RequestParam("dogName") String dogName,
                                                        @RequestParam("breed") String breed,
                                                        @RequestParam("age") int age) {
        CustomerDto updatedCustomer = customerService.addDogToCustomer(customerId, dogName, breed, age);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{customerId}/dogs/{dogId}")
    public ResponseEntity<Void> removeDogFromCustomer(@PathVariable Long customerId,
                                                      @PathVariable Long dogId) {
        try {
            customerService.removeDogFromCustomer(customerId, dogId);
            return ResponseEntity.ok().build();
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
