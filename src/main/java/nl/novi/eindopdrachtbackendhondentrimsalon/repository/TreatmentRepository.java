package nl.novi.eindopdrachtbackendhondentrimsalon.repository;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    //Find treatments by name
    List<Treatment> findByName(String name);
}
