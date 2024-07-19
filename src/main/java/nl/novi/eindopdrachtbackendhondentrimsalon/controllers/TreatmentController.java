package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.TreatmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    @Autowired
    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @GetMapping("/alltreatments")
    public ResponseEntity<List<TreatmentDto>> getAllTreatments() {
        List<TreatmentDto> treatments = treatmentService.getAllTreatments();
        return new ResponseEntity<>(treatments, HttpStatus.OK);
    }

    @GetMapping("/{treatmentId}")
    public ResponseEntity<TreatmentDto> getTreatmentById(@PathVariable Long treatmentId) {
        TreatmentDto treatmentDto = treatmentService.getTreatmentById(treatmentId);
        if (treatmentDto != null) {
            return new ResponseEntity<>(treatmentDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("")
    public ResponseEntity<List<TreatmentDto>> findTreatmentByName(@RequestParam String name) {
        List<TreatmentDto> treatments = treatmentService.findTreatmentByName(name);
        return ResponseEntity.ok(treatments);
    }

    @PostMapping("")
    public ResponseEntity<TreatmentDto> addTreatment(@RequestBody TreatmentDto treatmentDto) {
        TreatmentDto newTreatmentDto = treatmentService.addTreatment(treatmentDto);
        return new ResponseEntity<>(newTreatmentDto, HttpStatus.CREATED);
    }

    @PutMapping("/{treatmentId}")
    public ResponseEntity<TreatmentDto> updateTreatment(@PathVariable Long treatmentId,
                                                        @RequestBody TreatmentDto updatedTreatmentDto) {
        TreatmentDto treatmentDto = treatmentService.updateTreatment(treatmentId, updatedTreatmentDto);
        return ResponseEntity.ok(treatmentDto);
    }

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long treatmentId) {
        treatmentService.deleteTreatment(treatmentId);
        return ResponseEntity.noContent().build();
    }
}
