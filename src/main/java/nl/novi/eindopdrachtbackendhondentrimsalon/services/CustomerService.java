package nl.novi.eindopdrachtbackendhondentrimsalon.services;

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

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //Adding new customers to the system
    public void addCustomer(String customerName, String phoneNumber, String dogName, String breed, int age) {
        // Check if customer already exists
        Customer existingCustomer = customerRepository.findByName(customerName);

        if (existingCustomer != null) {
            // Customer already exists
            Dog existingDog = findDogInCustomer(existingCustomer, dogName);
            if (existingDog != null) {
                // Dog already exists for this customer
                throw new RuntimeException("Dog with name '" + dogName + "' already exists for customer '" + customerName + "'.");
            } else {
                // Add new dog to existing customer
                Dog newDog = new Dog();
                newDog.setName(dogName);
                newDog.setBreed(breed);
                newDog.setAge(age);
                newDog.setCustomer(existingCustomer); // Set the customer for the new dog
                existingCustomer.getDogs().add(newDog); // Add dog to the existing customer's list of dogs
                customerRepository.save(existingCustomer); // Update the customer in repository
            }
        } else {
            // Customer does not exist, create new customer and dog
            Customer newCustomer = new Customer(customerName, phoneNumber);
            Dog newDog = new Dog();
            newDog.setName(dogName);
            newDog.setBreed(breed);
            newDog.setAge(age);
            newDog.setCustomer(newCustomer); // Set the customer for the new dog
            newCustomer.getDogs().add(newDog); // Add dog to the new customer's list of dogs
            customerRepository.save(newCustomer); // Add the new customer to repository
        }
    }

    private Dog findDogInCustomer(Customer customer, String dogName) {
        List<Dog> dogs = customer.getDogs();
        for (Dog dog : dogs) {
            if (dog.getName().equals(dogName)) {
                return dog;
            }
        }
        return null; // Dog not found
    }

    //Updating customer information
    public Customer updateCustomer(Long customerId, Customer customer) {
        //Check if the customer with the given ID exists
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        //Update customer properties
        existingCustomer.setName(customer.getName());
        existingCustomer.setPhonenumber(customer.getPhonenumber());
        existingCustomer.setDogs(customer.getDogs());

        //Save the updated customer
        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long customerId) {
        //Check if the customer with the given ID exists
        if (customerRepository.findById(customerId).isPresent()) {
            throw new RuntimeException("Customer not found with this id: " + customerId);
        } else {
            customerRepository.deleteById(customerId);
        }
    }

    //Retrieving customer details
    public Customer getCustomerById(Long customerId) {
        //Retrieve customer by ID
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id" + customerId));
    }

    //Retrieve all customers
    public List<Customer> getAllCustomers() {
        // Retrieve all customers
        return customerRepository.findAll();
    }

    //Managing relationships with dogs (e.g. adding, updating, or removing dogs for a customer)

    public void addDogForCustomer(Long customerId, String dogName, String breed, int age) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        //Create new dog and associate it with the customer
        Dog newDog = new Dog(dogName, breed, age);
        newDog.setCustomer(customer);
        customer.getDogs().add(newDog);

        customerRepository.save(customer);
    }

    public void updateDogForCustomer(Long customerId, Long dogId, String dogName, String breed, int age) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        //Find the existingDog associated with the customer
        Dog existingDog = customer.getDogs().stream()
                .filter(dog -> dog.getId().equals(dogId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dog not found with id: " + dogId));

        //Update the dog's attributes
        existingDog.setName(dogName);
        existingDog.setBreed(breed);
        existingDog.setAge(age);

        customerRepository.save(customer);
    }

    public void removeDogFromCustomer(Long customerId, Long dogId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        //Find and remove the dog associated with the customer
        customer.getDogs().removeIf(dog -> dog.getId().equals(dogId));

        customerRepository.save(customer);
    }

}
