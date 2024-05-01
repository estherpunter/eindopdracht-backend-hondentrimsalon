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

    public Treatment addTreatment(Treatment treatment) {
        Optional<Treatment> existingTreatment = treatmentRepository.findById(treatment.getId());
        if (existingTreatment.isPresent()) {
            throw new RuntimeException("Treatment with ID " + treatment.getId() + " already exists.");
        }
        if (treatment.getName() == null || treatment.getPrice() <= 0) {
            throw new IllegalArgumentException("Treatment name and price are required.");
        }

        return treatmentRepository.save(treatment);
    }


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
