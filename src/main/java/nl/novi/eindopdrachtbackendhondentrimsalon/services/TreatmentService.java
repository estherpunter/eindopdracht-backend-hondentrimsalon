package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.TreatmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.TreatmentNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.TreatmentMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Treatment;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final TreatmentMapper treatmentMapper;

    @Autowired
    public TreatmentService(TreatmentRepository treatmentRepository, TreatmentMapper treatmentMapper) {
        this.treatmentRepository = treatmentRepository;
        this.treatmentMapper = treatmentMapper;
    }

    public List<TreatmentDto> getAllTreatments() {
        List<Treatment> treatments = treatmentRepository.findAll();
        return treatmentMapper.treatmentsToTreatmentDtos(treatments);
    }

    public TreatmentDto getTreatmentById(Long treatmentId) {
        Treatment treatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new TreatmentNotFoundException(treatmentId));
        return treatmentMapper.treatmentToTreatmentDto(treatment);
    }

    public List<TreatmentDto> findTreatmentByName(String name) {
        List<Treatment> treatments = treatmentRepository.findByNameIgnoreCase(name);
        return treatmentMapper.treatmentsToTreatmentDtos(treatments);
    }


    public TreatmentDto addTreatment(TreatmentDto treatmentDto) {
        if (treatmentDto.getId() != null && treatmentRepository.existsById(treatmentDto.getId())) {
            throw new RuntimeException("Treatment with ID " + treatmentDto.getId() + " already exists.");
        }
        if (treatmentDto.getName() == null || treatmentDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Treatment name and price are required.");
        }
        Treatment treatment = treatmentMapper.treatmentDtoToTreatment(treatmentDto);
        Treatment savedTreatment = treatmentRepository.save(treatment);
        return treatmentMapper.treatmentToTreatmentDto(savedTreatment);
    }


    public TreatmentDto updateTreatment(Long treatmentId, TreatmentDto updatedTreatmentDto) {
        Treatment existingTreatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new TreatmentNotFoundException(treatmentId));

        existingTreatment.setName(updatedTreatmentDto.getName());
        existingTreatment.setPrice(updatedTreatmentDto.getPrice());

        Treatment savedTreatment = treatmentRepository.save(existingTreatment);
        return treatmentMapper.treatmentToTreatmentDto(savedTreatment);
    }

    public void deleteTreatment(Long treatmentId) {
        if (!treatmentRepository.existsById(treatmentId)) {
            throw new TreatmentNotFoundException(treatmentId);
        }
        treatmentRepository.deleteById(treatmentId);
    }
}
