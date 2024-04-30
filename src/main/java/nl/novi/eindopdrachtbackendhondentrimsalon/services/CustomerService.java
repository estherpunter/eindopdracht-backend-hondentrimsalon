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


    //Retrieving customer details
    public Customer getCustomerById(Long customerId) {
        //Retrieve customer by ID
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id" + customerId));
    }

    //Retrieve all customers
    public List<Customer> getAllCustomers() {
        // Retrieve all customers
        return customerRepository.findAll();
    }

    //Adding new customers to the system
    public Customer addCustomer(String customerName, String phoneNumber, String dogName, String breed, int age) {
        // Check if customer already exists
        Customer customer = customerRepository.findByName(customerName);

        if (customer != null) {
            // Customer already exists
            List<Dog> dogs = dogRepository.findByCustomer(customer);
            for (Dog dog : dogs) {
                if (dog.getName().equalsIgnoreCase(dogName)) {
                    throw new RuntimeException("Dog with name '" + dogName + "' already exists for customer '" + customerName + "'.");
                }
            }
            // Add new dog to existing customer
            Dog newDog = new Dog();
            newDog.setName(dogName);
            newDog.setBreed(breed);
            newDog.setAge(age);
            newDog.setCustomer(customer); // Set the customer for the new dog
            customer.getDogs().add(newDog); // Add dog to the existing customer's list of dogs
            return customerRepository.save(customer); // Update the customer in repository
        } else {
            // Customer does not exist, create new customer and dog
            Customer newCustomer = new Customer(customerName, phoneNumber);
            Dog newDog = new Dog();
            newDog.setName(dogName);
            newDog.setBreed(breed);
            newDog.setAge(age);
            newDog.setCustomer(newCustomer); // Set the customer for the new dog
            newCustomer.getDogs().add(newDog); // Add dog to the new customer's list of dogs
            return customerRepository.save(newCustomer); // Add the new customer to repository
        }
    }

    //Updating customer information
    public Customer updateCustomer(Long customerId, Customer customer) {
        //Check if the customer with the given ID exists
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + customerId));

        //Update customer properties
        existingCustomer.setName(customer.getName());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setDogs(customer.getDogs());

        //Save the updated customer
        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long customerId) {
        //Check if the customer with the given ID exists
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

        Dog newDog = new Dog(dogName, breed, age);
        newDog.setCustomer(customer);
        customer.getDogs().add(newDog);

        return customerRepository.save(customer);
    }

    public void updateDogForCustomer(Long customerId, Long dogId, String dogName, String breed, int age) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + customerId));

        //Find the existingDog associated with the customer
        Dog existingDog = customer.getDogs().stream()
                .filter(dog -> dog.getId().equals(dogId))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("Dog not found with id: " + dogId));

        //Update the dog's attributes
        existingDog.setName(dogName);
        existingDog.setBreed(breed);
        existingDog.setAge(age);

        dogRepository.save(existingDog);
    }

    public void removeDogFromCustomer(Long customerId, Long dogId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + customerId));

        //Find and remove the dog associated with the customer
        customer.getDogs().removeIf(dog -> dog.getId().equals(dogId));

        customerRepository.save(customer);
    }

}
