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
    private final DogRepository dogRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, DogRepository dogRepository) {
        this.customerRepository = customerRepository;
        this.dogRepository = dogRepository;
    }


    public Customer createCustomer(Customer customer) {
        // Check if customer already exists
        Customer existingCustomer = customerRepository.findByName(customer.getName());

        if (existingCustomer != null) {
            // Customer already exists, check if dog also exists for this customer
            List<Dog> existingDogs = existingCustomer.getDogs();
            boolean dogExists = false;

            for (Dog existingDog : existingDogs) {
                if (existingDog.getName().equals(customer.getDogs().getName())) {
                    //Both customer and dog already exist
                    throw new RuntimeException("This customer and dog are already registered");
                }
            }

            //Dog does not exist, add the new dog to the existing customer
            Dog newDog = customer.getDogs().get(0); //Assuming only one dog is being added
            newDog.setCustomer(existingCustomer); //Associate the dog with the existing custoemr
            existingDogs.add(newDog); //Add the new dog to the list of existing dogs
            existingCustomer.setDogs(existingDogs); //Update the list of dogs for the existing customer
            return customerRepository.save(existingCustomer);
        } else {
            //Customer does not exist, save the new customer
            return customerRepository.save(customer);
        }
    }

    public Customer updateCustomer(Long customerId, Customer customer) {
        //Check if the customer with the given ID exists
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        //Update customer properties
        existingCustomer.setName(customer.getName());
        existingCustomer.setPhonenumber(customer.getPhonenumber());

        //Save the updated customer
        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long customerId) {
        //Check if the customer with the given ID exists
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with this id: " + customerId));

        //Delete the customer
        customerRepository.delete(existingCustomer);
    }

    public Customer getCustomerById(Long customerId) {
        //Retrieve customer by ID
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id" + customerId));
    }


    public List<Customer> getAllCustomers() {
        // Retrieve all customers
        return customerRepository.findAll();
    }
}
