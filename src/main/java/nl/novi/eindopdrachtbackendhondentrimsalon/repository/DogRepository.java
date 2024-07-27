package nl.novi.eindopdrachtbackendhondentrimsalon.repository;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

    List<Dog> findByCustomer(Customer customer);
    List<Dog> findByCustomerId(Long customerId);

}
