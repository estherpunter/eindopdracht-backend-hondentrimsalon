package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.TreatmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.TreatmentRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {

    private final TreatmentService treatmentService;

    @Autowired
    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @GetMapping
    public ResponseEntity<List<TreatmentDto>> getAllTreatments() {
        List<TreatmentDto> treatmentDtos = treatmentService.getAllTreatments();
        return new ResponseEntity<>(treatmentDtos, HttpStatus.OK);
    }

    @GetMapping("/{treatmentId}")
    public ResponseEntity<TreatmentDto> getTreatmentById(@PathVariable Long treatmentId) {
        TreatmentDto treatmentDto = treatmentService.getTreatmentById(treatmentId);
        return ResponseEntity.ok(treatmentDto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TreatmentDto>> findTreatmentByName(@RequestParam String name) {
        List<TreatmentDto> treatments = treatmentService.findTreatmentByName(name);
        return ResponseEntity.ok(treatments);
    }

    @PostMapping
    public ResponseEntity<Void> addTreatment(@RequestBody TreatmentRequestDto treatmentRequestDto) {
        TreatmentDto newTreatmentDto = treatmentService.addTreatment(treatmentRequestDto);

        Long treatmentId = newTreatmentDto.getId();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(treatmentId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{treatmentId}")
    public ResponseEntity<TreatmentDto> updateTreatment(@PathVariable Long treatmentId,
                                                        @RequestBody TreatmentRequestDto treatmentRequestDto) {
        TreatmentDto treatmentDto = treatmentService.updateTreatment(treatmentId, treatmentRequestDto);
        return ResponseEntity.ok(treatmentDto);
    }

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long treatmentId) {
        treatmentService.deleteTreatment(treatmentId);
        return ResponseEntity.ok().build();
    }
}
