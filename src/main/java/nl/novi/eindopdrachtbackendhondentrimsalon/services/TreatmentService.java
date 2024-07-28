package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.TreatmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.TreatmentRequestDto;
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


    public TreatmentDto addTreatment(TreatmentRequestDto treatmentRequestDto) {
        if (treatmentRequestDto.getTreatmentName() == null || treatmentRequestDto.getTreatmentName().trim().isEmpty() || treatmentRequestDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Treatment name and price are required.");
        }

        List<Treatment> treatments = treatmentRepository.findByNameIgnoreCase(treatmentRequestDto.getTreatmentName());
        if (!treatments.isEmpty()) {
            throw new IllegalArgumentException("Treatment with this name already exists.");
        }

        Treatment treatment = new Treatment();
        treatment.setName(treatmentRequestDto.getTreatmentName());
        treatment.setPrice(treatmentRequestDto.getPrice());

        Treatment newTreatment = treatmentRepository.save(treatment);

        return treatmentMapper.treatmentToTreatmentDto(newTreatment);
    }


    public TreatmentDto updateTreatment(Long treatmentId, TreatmentRequestDto treatmentRequestDto) {
        Treatment treatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new TreatmentNotFoundException(treatmentId));

        treatment.setName(treatmentRequestDto.getTreatmentName());
        treatment.setPrice(treatmentRequestDto.getPrice());

        Treatment savedTreatment = treatmentRepository.save(treatment);

        return treatmentMapper.treatmentToTreatmentDto(savedTreatment);
    }

    public void deleteTreatment(Long treatmentId) {
        if (!treatmentRepository.existsById(treatmentId)) {
            throw new TreatmentNotFoundException(treatmentId);
        }
        treatmentRepository.deleteById(treatmentId);
    }
}
