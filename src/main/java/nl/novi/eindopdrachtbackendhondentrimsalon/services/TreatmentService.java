package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Treatment;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;

    @Autowired
    public TreatmentService(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    //Retrieving treatment details
    public List<Treatment> getAllTreatments() {
        return treatmentRepository.findAll();
    }

    public Treatment getTreatmentById(Long treatmentId) {
        return treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new RecordNotFoundException("Treatment not found with ID: " + treatmentId));
    }

    public List<Treatment> findTreatmentByName(String name) {
        return treatmentRepository.findByNameIgnoreCase(name);
   }

    //Adding new treatments to the system
    public Treatment addTreatment(Treatment treatment) {
        //Perform any additional validation or business logic before saving the product
        return treatmentRepository.save(treatment);
    }

    //Updating treatment information (e.g. name, price)
    public Treatment updateTreatment(Long treatmentId, Treatment updatedTreatment) {
        Treatment existingTreatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new RecordNotFoundException("Treatment not found with id: " + treatmentId));

        existingTreatment.setName(updatedTreatment.getName());
        existingTreatment.setPrice(updatedTreatment.getPrice());

        return treatmentRepository.save(existingTreatment);
    }

    public void deleteTreatment(Long treatmentId) {
        Optional<Treatment> treatmentOptional = treatmentRepository.findById(treatmentId);
        if (treatmentOptional.isPresent()) {
            treatmentRepository.deleteById(treatmentId);
        } else {
            throw new RecordNotFoundException("Treatment not found with id: " + treatmentId);
        }
    }
}
