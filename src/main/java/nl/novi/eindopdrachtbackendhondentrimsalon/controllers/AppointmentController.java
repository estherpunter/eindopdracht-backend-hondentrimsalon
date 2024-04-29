package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Appointment;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping("/{appointmentId}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long appointmentId, @RequestParam LocalDateTime newDate, @RequestParam String newStatus) {
        Appointment updatedAppointment = appointmentService.updateAppointment(appointmentId, newDate, newStatus);
        return ResponseEntity.ok(updatedAppointment);
    }

    @PostMapping("/{appointmentId}/custom-treatment")
    public ResponseEntity<Appointment> addCustomTreatmentToAppointment(@PathVariable Long appointmentId, @RequestParam double customPrice) {
        Appointment updatedAppointment = appointmentService.addCustomTreatmentToAppointment(appointmentId, customPrice);
        return ResponseEntity.ok(updatedAppointment);
    }

}
