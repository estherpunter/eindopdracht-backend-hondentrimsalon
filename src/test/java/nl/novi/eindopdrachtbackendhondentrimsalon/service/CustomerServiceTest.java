package nl.novi.eindopdrachtbackendhondentrimsalon.service;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.CustomerNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.DogNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.CustomerMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.CustomerRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.DogRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

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
    public void testAddCustomer_ValidCustomer() {
        // Arrange
        String customerName = "John Doe";
        String phoneNumber = "1234567890";
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(customerName);
        customerDto.setPhoneNumber(phoneNumber);

        Customer savedCustomer = new Customer(customerName, phoneNumber);
        when(customerMapper.customerToCustomerDto(any())).thenReturn(customerDto);
        when(customerRepository.save(any())).thenReturn(savedCustomer);

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

//    @Test
//    void testUpdateCustomer_ValidCustomerId() {
//        // Arrange
//        long customerId = 1L;
//        CustomerDto customerDto = new CustomerDto();
//        customerDto.setName("Updated Name");
//        customerDto.setPhoneNumber("9876543210");
//        Customer existingCustomer = new Customer();
//        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
//        when(customerMapper.customerToCustomerDto(any())).thenReturn(customerDto);
//
//        // Act
//        CustomerDto result = customerService.updateCustomer(customerId, customerDto);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(customerDto.getName(), result.getName());
//        assertEquals(customerDto.getPhoneNumber(), result.getPhoneNumber());
//    }

    @Test
    void testGetAllCustomers_WithExistingCustomers() {
        // Arrange
        List<Customer> customers = Collections.singletonList(new Customer());
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.customersToCustomerDtos(customers)).thenReturn(Collections.singletonList(new CustomerDto()));

        // Act
        List<CustomerDto> result = customerService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(customers.size(), result.size());
    }

    @Test
    void testGetAllCustomers_WithNoExistingCustomers() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<CustomerDto> result = customerService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteCustomer_WithExistingCustomerId() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> customerService.deleteCustomer(customerId));

        // Assert
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void testDeleteCustomer_WithNonExistingCustomerId() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        // Act + Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(customerId));
        verify(customerRepository, never()).deleteById(customerId);
    }

    @Test
    void testAddDogToCustomer_DuplicateDogName() {
        // Arrange
        long customerId = 1L;
        String dogName = "Buddy";
        String breed = "Labrador";
        int age = 3;

        Customer customer = new Customer();
        Dog existingDog = new Dog();
        existingDog.setName(dogName);
        customer.getDogs().add(existingDog);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Mock dogRepository behavior to return a list with an existing dog with the same name
        List<Dog> existingDogs = Collections.singletonList(existingDog);
        when(dogRepository.findByCustomer(customer)).thenReturn(existingDogs);

        // Act + Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.addDogToCustomer(customerId, dogName, breed, age);
        });

        assertEquals("Dog '" + dogName + "' already exists for customer.", exception.getMessage());
    }

    @Test
    void testUpdateDogForCustomer_WithExistingDogId() {
        // Arrange
        long customerId = 1L;
        long dogId = 1L;
        String dogName = "Updated Dog";
        String breed = "Poodle";
        int age = 5;
        Customer customer = new Customer();
        Dog existingDog = new Dog();
        existingDog.setId(dogId);
        customer.getDogs().add(existingDog);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        assertDoesNotThrow(() -> customerService.updateDogForCustomer(customerId, dogId, dogName, breed, age));

        // Assert
        assertEquals(dogName, existingDog.getName());
        assertEquals(breed, existingDog.getBreed());
        assertEquals(age, existingDog.getAge());
        verify(dogRepository, times(1)).save(existingDog);
    }

    @Test
    void testUpdateDogForCustomer_WithNonExistingDogId() {
        // Arrange
        long customerId = 1L;
        long dogId = 1L;
        String dogName = "Updated Dog";
        String breed = "Poodle";
        int age = 5;
        Customer customer = new Customer();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act + Assert
        assertThrows(DogNotFoundException.class, () -> customerService.updateDogForCustomer(customerId, dogId, dogName, breed, age));
        verify(dogRepository, never()).save(any());
    }


    @Test
    void testRemoveDogFromCustomer_ValidDogId() {
        // Arrange
        Long customerId = 1L;
        Long dogId = 100L;

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setDogs(new HashSet<>());

        Dog dog = new Dog();
        dog.setId(dogId);
        dog.setName("Buddy");
        dog.setBreed("Labrador");
        dog.setAge(3);
        customer.getDogs().add(dog);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        assertDoesNotThrow(() -> customerService.removeDogFromCustomer(customerId, dogId));

        // Assert
        assertEquals(0, customer.getDogs().size());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testRemoveDogFromCustomer_CustomerNotFound() {
        // Arrange
        Long customerId = 1L;
        Long dogId = 100L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act + Assert
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
                () -> customerService.removeDogFromCustomer(customerId, dogId));

        assertEquals("Cannot find customer with Id: " + customerId, exception.getMessage());
        verify(customerRepository, never()).save(any());
    }
}


