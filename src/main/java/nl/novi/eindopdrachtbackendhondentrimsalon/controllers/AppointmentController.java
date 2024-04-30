package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Appointment;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Receipt;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<Appointment> scheduleAppointment(@RequestParam Long customerId,
                                                           @RequestParam Long dogId,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentDate) {
        try {
            Appointment appointment = appointmentService.scheduleAppointment(customerId, dogId, appointmentDate);
            return ResponseEntity.ok(appointment);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<Void> updateAppointment(@PathVariable Long appointmentId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime appointmentDate,
                                                  @RequestParam String status) {
        try {
            appointmentService.updateAppointment(appointmentId, appointmentDate, status);
            return ResponseEntity.ok().build();
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.cancelAppointment(appointmentId);
            return ResponseEntity.noContent().build();
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{appointmentId}/products")
    public ResponseEntity<Void> addProductToAppointment(
            @PathVariable Long appointmentId,
            @RequestParam Long productId
    ) {
        try {
            appointmentService.addProductToAppointment(appointmentId, productId);
            return ResponseEntity.ok().build();
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{appointmentId}/treatments")
    public ResponseEntity<Void> addTreatmentToAppointment(
            @PathVariable Long appointmentId,
            @RequestParam Long treatmentId
    ) {
        try {
            appointmentService.addTreatmentToAppointment(appointmentId, treatmentId);
            return ResponseEntity.ok().build();
        } catch (RecordNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
