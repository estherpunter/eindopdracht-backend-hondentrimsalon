package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import jakarta.validation.ValidationException;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.DogDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.DogRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.CustomerNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.DogNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.CustomerMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.DogMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.CustomerRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private DogRepository dogRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private DogMapper dogMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDto customerDto;
    private CustomerRequestDto customerRequestDto;
    private Dog dog;
    private DogDto dogDto;
    private DogRequestDto dogRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setPhoneNumber("123456789");

        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setName("John Doe");
        customerDto.setPhoneNumber("123456789");

        customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setCustomerName("John Doe");
        customerRequestDto.setPhoneNumber("123456789");

        dog = new Dog();
        dog.setId(1L);
        dog.setName("Buddy");
        dog.setBreed("Golden Retriever");
        dog.setAge(3);

        dogDto = new DogDto();
        dogDto.setId(1L);
        dogDto.setName("Buddy");
        dogDto.setBreed("Golden Retriever");
        dogDto.setAge(3);

        dogRequestDto = new DogRequestDto();
        dogRequestDto.setDogName("Buddy");
        dogRequestDto.setBreed("Golden Retriever");
        dogRequestDto.setAge(3);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getAllCustomers() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        when(customerMapper.customersToCustomerDtos(anyList())).thenReturn(Collections.singletonList(customerDto));

        // Act
        List<CustomerDto> result = customerService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getCustomerById() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(customerDto);

        // Act
        CustomerDto result = customerService.getCustomerById(customerId);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("123456789", result.getPhoneNumber());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getCustomerById_CustomerNotFoundException() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(customerId));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void addCustomer() {
        // Arrange
        when(customerRepository.findByName(anyString())).thenReturn(null);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(customerDto);

        // Act
        CustomerDto result = customerService.addCustomer(customerRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("123456789", result.getPhoneNumber());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void addCustomer_ValidationException() {
        // Arrange
        when(customerRepository.findByName(anyString())).thenReturn(customer);

        // Act & Assert
        assertThrows(ValidationException.class, () -> customerService.addCustomer(customerRequestDto));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void updateCustomer() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(customerDto);

        // Act
        CustomerDto result = customerService.updateCustomer(customerId, customerRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("123456789", result.getPhoneNumber());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void updateCustomer_CustomerNotFoundException() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(customerId, customerRequestDto));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void deleteCustomer() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(any(Customer.class));

        // Act
        customerService.deleteCustomer(customerId);

        // Assert
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void deleteCustomer_CustomerNotFoundException() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(customerId));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void addDogToCustomer() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dogRepository.findByCustomer(any(Customer.class))).thenReturn(Collections.emptyList());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(customerDto);

        // Act
        CustomerDto result = customerService.addDogToCustomer(customerId, dogRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("123456789", result.getPhoneNumber());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void addDogToCustomer_ValidationException() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dogRepository.findByCustomer(any(Customer.class))).thenReturn(Collections.singletonList(dog));

        // Act & Assert
        assertThrows(ValidationException.class, () -> customerService.addDogToCustomer(customerId, dogRequestDto));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void removeDogFromCustomer() {
        // Arrange
        long customerId = 1L;
        long dogId = 1L;

        Customer customer = new Customer();
        customer.setId(customerId);

        Dog dog = new Dog();
        dog.setId(dogId);
        dog.setCustomer(customer);

        customer.setDogs(new HashSet<>(Collections.singletonList(dog)));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dogRepository.findById(dogId)).thenReturn(Optional.of(dog));

        // Act
        customerService.removeDogFromCustomer(customerId, dogId);

        // Assert
        assertNotNull(customer.getDogs());
        assertFalse(customer.getDogs().contains(dog));
        assertNull(dog.getCustomer());
        verify(customerRepository, times(1)).save(customer);
        verify(dogRepository, times(1)).save(dog);
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void removeDogFromCustomer_CustomerNotFoundException() {
        // Arrange
        long customerId = 1L;
        long dogId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.removeDogFromCustomer(customerId, dogId));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void removeDogFromCustomer_DogNotFoundException() {
        // Arrange
        long customerId = 1L;
        long dogId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dogRepository.findById(dogId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DogNotFoundException.class, () -> customerService.removeDogFromCustomer(customerId, dogId));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void removeDogFromCustomer_ValidationException() {
        // Arrange
        long customerId = 1L;
        long dogId = 2L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dogRepository.findById(dogId)).thenReturn(Optional.of(dog));

        // Act & Assert
        assertThrows(ValidationException.class, () -> customerService.removeDogFromCustomer(customerId, dogId));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getDogsByCustomerId() {
        // Arrange
        long customerId = 1L;
        List<Dog> dogs = Collections.singletonList(dog);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dogRepository.findByCustomerId(customerId)).thenReturn(dogs);
        when(dogMapper.dogToDogDto(any(Dog.class))).thenReturn(dogDto);

        // Act
        List<DogDto> result = customerService.getDogsByCustomerId(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Buddy", result.get(0).getName());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getDogsByCustomerId_CustomerNotFoundException() {
        // Arrange
        long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, () -> customerService.getDogsByCustomerId(customerId));
    }
}
