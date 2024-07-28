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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final DogRepository dogRepository;
    private final CustomerMapper customerMapper;
    private final DogMapper dogMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, DogRepository dogRepository, CustomerMapper customerMapper, DogMapper dogMapper) {
        this.customerRepository = customerRepository;
        this.dogRepository = dogRepository;
        this.customerMapper = customerMapper;
        this.dogMapper = dogMapper;
    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        return customerMapper.customersToCustomerDtos(customers);
    }

    public CustomerDto getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        return customerMapper.customerToCustomerDto(customer);
    }

    public List<DogDto> getDogsByCustomerId(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<Dog> dogs = dogRepository.findByCustomerId(customerId);

        return dogs.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CustomerDto addCustomer(CustomerRequestDto customerRequestDto) {
        Customer customer = customerRepository.findByName(customerRequestDto.getCustomerName());

        if (customer != null) {
            throw new ValidationException("Customer with name: " + customerRequestDto.getCustomerName() + " already exists.");
        } else {
            Customer newCustomer = new Customer(customerRequestDto.getCustomerName(), customerRequestDto.getPhoneNumber());
            Customer savedCustomer = customerRepository.save(newCustomer);

            return customerMapper.customerToCustomerDto(savedCustomer);
        }
    }

    public CustomerDto updateCustomer(Long customerId, CustomerRequestDto customerRequestDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        customer.setName(customerRequestDto.getCustomerName());
        customer.setPhoneNumber(customerRequestDto.getPhoneNumber());

        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.customerToCustomerDto(updatedCustomer);
    }

    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        customerRepository.delete(customer);
    }

    public CustomerDto addDogToCustomer(Long customerId, DogRequestDto dogRequestDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<Dog> dogs = dogRepository.findByCustomer(customer);
        for (Dog dog : dogs) {
            if (dog.getName().equalsIgnoreCase(dogRequestDto.getDogName())) {
                throw new ValidationException("Dog '" + dogRequestDto.getDogName() + "' already exists for customer.");
            }
        }

        Dog newDog = new Dog();
        newDog.setName(dogRequestDto.getDogName());
        newDog.setBreed(dogRequestDto.getBreed());
        newDog.setAge(dogRequestDto.getAge());

        newDog.setCustomer(customer);
        customer.getDogs().add(newDog);

        Customer updatedCustomer = customerRepository.save(customer);

        return customerMapper.customerToCustomerDto(updatedCustomer);
    }

    public void removeDogFromCustomer(Long customerId, Long dogId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new DogNotFoundException(dogId));

        if (dog.getCustomer() != null && dog.getCustomer().getId().equals(customerId)) {
            customer.getDogs().remove(dog);
            dog.setCustomer(null);

            customerRepository.save(customer);
            dogRepository.save(dog);
        } else {
            throw new ValidationException("Dog does not belong to this customer");
        }
    }

    private DogDto convertToDto(Dog dog) {
        DogDto dto = new DogDto();
        dto.setId(dog.getId());
        dto.setName(dog.getName());
        dto.setBreed(dog.getBreed());
        dto.setAge(dog.getAge());
        return dto;
    }
}