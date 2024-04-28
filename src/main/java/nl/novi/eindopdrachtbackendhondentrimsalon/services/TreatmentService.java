package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
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
        Optional<Treatment> treatmentOptional = treatmentRepository.findById(treatmentId);
        return treatmentOptional.orElse(null);
    }


    //Adding new treatments to the system
    public Treatment addTreatment(Treatment treatment) {
        //Perform any additional validation or business logic before saving the product
        return treatmentRepository.save(treatment);
    }

    //Updating treatment information (e.g. name, price)
    public void updateTreatment(Long treatmentId, Treatment updatedTreatment) {
        Optional<Treatment> treatmentOptional = treatmentRepository.findById(treatmentId);
        if (treatmentOptional.isPresent()) {
            Treatment existingTreatment = treatmentOptional.get();

            existingTreatment.setName(updatedTreatment.getName());
            existingTreatment.setPrice(updatedTreatment.getPrice());

            treatmentRepository.save(existingTreatment);
        } else {
            throw new RuntimeException("Treatment not found with id: " + treatmentId); //Treatment not found
        }
    }

    public boolean deleteTreatment(Long treatmentId) {
        Optional<Treatment> treatmentOptional = treatmentRepository.findById(treatmentId);
        if (treatmentOptional.isPresent()) {
            treatmentRepository.deleteById(treatmentId);
            return true; //Treatment successfully deleted
        }
        return false; //Treatment not found
    }

    //Associating treatments with appointments

}
