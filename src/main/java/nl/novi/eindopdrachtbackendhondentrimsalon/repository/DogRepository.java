package nl.novi.eindopdrachtbackendhondentrimsalon.repository;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

}
