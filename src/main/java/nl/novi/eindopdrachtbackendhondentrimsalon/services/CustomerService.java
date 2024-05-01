package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
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

    @Autowired
    public CustomerService(CustomerRepository customerRepository, DogRepository dogRepository) {
        this.customerRepository = customerRepository;
        this.dogRepository = dogRepository;
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id" + customerId));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer addCustomer(String customerName, String phoneNumber) {
        Customer exisitingCustomer = customerRepository.findByName(customerName);

        if (exisitingCustomer != null) {
            throw new RuntimeException("Customer with name: " + customerName + " already exists.");
        } else {
            Customer newCustomer = new Customer(customerName, phoneNumber);
            return customerRepository.save(newCustomer);
        }
    }

//    private void checkIfDogExistsForCustomer(Customer customer, String dogName) {
//        List<Dog> dogs = customer.getDogs();
//        for (Dog dog : dogs) {
//            if (dog.getName().equalsIgnoreCase(dogName)) {
//                throw new RuntimeException("Dog with name '" + dogName + "' already exists for customer '" + customer.getName() + "'.");
//            }
//        }
//    }

    public Customer updateCustomer(Long customerId, Customer customer) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + customerId));

        existingCustomer.setName(customer.getName());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setDogs(customer.getDogs());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long customerId) {
        if (customerRepository.findById(customerId).isPresent()) {
            throw new RecordNotFoundException("Customer not found with id: " + customerId);
        } else {
            customerRepository.deleteById(customerId);
        }
    }

    public Customer addDogToCustomer(Long customerId, String dogName, String breed, int age) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + customerId));

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

        return customerRepository.save(customer);
    }

    public void updateDogForCustomer(Long customerId, Long dogId, String dogName, String breed, int age) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + customerId));

        Dog existingDog = customer.getDogs().stream()
                .filter(dog -> dog.getId().equals(dogId))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("Dog not found with id: " + dogId));

        existingDog.setName(dogName);
        existingDog.setBreed(breed);
        existingDog.setAge(age);

        dogRepository.save(existingDog);
    }

    public void removeDogFromCustomer(Long customerId, Long dogId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + customerId));

        customer.getDogs().removeIf(dog -> dog.getId().equals(dogId));

        customerRepository.save(customer);
    }
}
