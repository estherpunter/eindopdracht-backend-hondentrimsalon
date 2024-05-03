package nl.novi.eindopdrachtbackendhondentrimsalon.service;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.CustomerNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.CustomerMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.CustomerRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.DogRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DogRepository dogRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetCustomerById_ExistingId() {
        // Arrange
        long customerId = 1L;
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerDto(customer)).thenReturn(new CustomerDto());

        // Act
        CustomerDto result = customerService.getCustomerById(customerId);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testGetCustomerById_NonExistingId() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    void testAddCustomer_ValidCustomer() {
        // Arrange
        String customerName = "John Doe";
        String phoneNumber = "1234567890";
        when(customerRepository.findByName(customerName)).thenReturn(null);

        // Act
        CustomerDto result = customerService.addCustomer(customerName, phoneNumber);

        // Assert
        assertNotNull(result);
        assertEquals(customerName, result.getName());
        assertEquals(phoneNumber, result.getPhoneNumber());
    }

    @Test
    void testAddCustomer_DuplicateCustomerName() {
        // Arrange
        String customerName = "John Doe";
        String phoneNumber = "1234567890";
        Customer existingCustomer = new Customer(customerName, phoneNumber);
        when(customerRepository.findByName(customerName)).thenReturn(existingCustomer);

        // Act + Assert
        assertThrows(RuntimeException.class, () -> customerService.addCustomer(customerName, phoneNumber));
    }

    @Test
    void testUpdateCustomer_ValidCustomerId() {
        // Arrange
        long customerId = 1L;
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName("Updated Name");
        customerDto.setPhoneNumber("9876543210");
        Customer existingCustomer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerMapper.customerToCustomerDto(any())).thenReturn(customerDto);

        // Act
        CustomerDto result = customerService.updateCustomer(customerId, customerDto);

        // Assert
        assertNotNull(result);
        assertEquals(customerDto.getName(), result.getName());
        assertEquals(customerDto.getPhoneNumber(), result.getPhoneNumber());
    }

    // More test methods for other scenarios...

}
