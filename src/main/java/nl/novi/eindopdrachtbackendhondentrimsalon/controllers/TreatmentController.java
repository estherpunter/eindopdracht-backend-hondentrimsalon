package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Treatment;
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
    public ResponseEntity<List<Treatment>> getAllTreatments() {
        List<Treatment> treatments = treatmentService.getAllTreatments();
        return new ResponseEntity<>(treatments, HttpStatus.OK);
    }

    @GetMapping("/{treatmentId}")
    public ResponseEntity<Treatment> getTreatmentById(@PathVariable Long treatmentId) {
        Treatment treatment = treatmentService.getTreatmentById(treatmentId);
        if (treatment != null) {
            return new ResponseEntity<>(treatment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Treatment>> findTreatmentByName(@RequestParam String name) {
        List<Treatment> treatments = treatmentService.findTreatmentByName(name);
        return ResponseEntity.ok(treatments);
    }

    @PostMapping
    public ResponseEntity<Treatment> addTreatment(@RequestBody Treatment treatment) {
        Treatment newTreatment = treatmentService.addTreatment(treatment);
        return new ResponseEntity<>(newTreatment, HttpStatus.CREATED);
    }

    @PutMapping("/{treatmentId}")
    public ResponseEntity<Treatment> updateTreatment(@PathVariable Long treatmentId,
                                                     @RequestBody Treatment updatedTreatment) {
        Treatment treatment = treatmentService.updateTreatment(treatmentId, updatedTreatment);
        return ResponseEntity.ok(treatment);
    }

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long treatmentId) {
        treatmentService.deleteTreatment(treatmentId);
        return ResponseEntity.noContent().build();
    }
}
