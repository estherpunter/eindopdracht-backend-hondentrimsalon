package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Appointment;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Receipt;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    @GetMapping("/allAppointments")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointmentDtos = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointmentDtos);
    }


    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long appointmentId) {
        AppointmentDto appointmentDto = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointmentDto);
    }


    @PostMapping("")
    public ResponseEntity<AppointmentDto> scheduleAppointment(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto scheduledAppointmentDto = appointmentService.scheduleAppointment(appointmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduledAppointmentDto);
    }


    @PutMapping("/{appointmentId}")
    public ResponseEntity<Void> updateAppointment(@PathVariable Long appointmentId, @RequestBody AppointmentDto appointmentDto) {
        appointmentDto.setId(appointmentId);
        appointmentService.updateAppointment(appointmentDto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{appointmentId}/products/{productId}")
    public ResponseEntity<Void> addProductToAppointment(@PathVariable Long appointmentId, @PathVariable Long productId) {
        appointmentService.addProductToAppointment(appointmentId, productId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{appointmentId}/treatments/{treatmentId}")
    public ResponseEntity<Void> addTreatmentToAppointment(@PathVariable Long appointmentId, @PathVariable Long treatmentId) {
        appointmentService.addTreatmentToAppointment(appointmentId, treatmentId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{appointmentId}/custom-treatment")
    public ResponseEntity<Appointment> addCustomTreatmentToAppointment(@PathVariable Long appointmentId, @RequestParam double customPrice) {
        Appointment updatedAppointment = appointmentService.addCustomTreatmentToAppointment(appointmentId, customPrice);
        return ResponseEntity.ok(updatedAppointment);
    }


    @PostMapping("/{appointmentId}/generate-receipt")
    public ResponseEntity<Receipt> generateReceipt(@PathVariable Long appointmentId) {
        Receipt receipt = appointmentService.generateReceipt(appointmentId);
        return ResponseEntity.ok(receipt);
    }
}
