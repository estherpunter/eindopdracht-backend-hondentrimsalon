package nl.novi.eindopdrachtbackendhondentrimsalon.repository;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Long> {

    //Find customer by name
    Customer findByName(String name);


}
