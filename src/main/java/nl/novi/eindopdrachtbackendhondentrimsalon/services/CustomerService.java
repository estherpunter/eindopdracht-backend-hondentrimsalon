package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.CustomerNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.DogNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.CustomerMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.CustomerRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final DogRepository dogRepository;

    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, DogRepository dogRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.dogRepository = dogRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerDto getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        return customerMapper.customerToCustomerDto(customer);
    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.customersToCustomerDtos(customers);
    }

    public CustomerDto addCustomer(String customerName, String phoneNumber) {
        Customer existingCustomer = customerRepository.findByName(customerName);

        if (existingCustomer != null) {
            throw new RuntimeException("Customer with name: " + customerName + " already exists.");
        } else {
            Customer newCustomer = new Customer(customerName, phoneNumber);
            Customer savedCustomer = customerRepository.save(newCustomer);
            return customerMapper.customerToCustomerDto(savedCustomer);
        }
    }

    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        existingCustomer.setName(customerDto.getName());
        existingCustomer.setPhoneNumber(customerDto.getPhoneNumber());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.customerToCustomerDto(updatedCustomer);
    }

    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        customerRepository.deleteById(customerId);
    }

    public CustomerDto addDogToCustomer(Long customerId, String dogName, String breed, int age) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        List<Dog> dogs = dogRepository.findByCustomer(customer);
        for (Dog dog : dogs) {
            if (dog.getName().equalsIgnoreCase(dogName)) {
                throw new RuntimeException("Dog '" + dogName + "' already exists for customer.");
            }
        }

        Dog newDog = new Dog();
        newDog.setName(dogName);
        newDog.setBreed(breed);
        newDog.setAge(age);

        newDog.setCustomer(customer);
        customer.getDogs().add(newDog);

        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.customerToCustomerDto(updatedCustomer);
    }

    public void updateDogForCustomer(Long customerId, Long dogId, String dogName, String breed, int age) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Dog existingDog = customer.getDogs().stream()
                .filter(dog -> dog.getId().equals(dogId))
                .findFirst()
                .orElseThrow(() -> new DogNotFoundException(dogId));

        existingDog.setName(dogName);
        existingDog.setBreed(breed);
        existingDog.setAge(age);

        dogRepository.save(existingDog);
    }

    public void removeDogFromCustomer(Long customerId, Long dogId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        customer.getDogs().removeIf(dog -> dog.getId().equals(dogId));

        customerRepository.save(customer);
    }

}