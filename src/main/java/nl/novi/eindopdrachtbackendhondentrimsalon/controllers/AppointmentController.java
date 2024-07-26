package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Receipt;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }


    @GetMapping("/allappointments")
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
    public ResponseEntity<AppointmentDto> scheduleAppointment(@RequestParam("date") LocalDateTime date,
                                                              @RequestParam("customerId") Long customerId,
                                                              @RequestParam("dogId") Long dogId) {
        AppointmentDto scheduledAppointmentDto = appointmentService.scheduleAppointment(date, customerId, dogId);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduledAppointmentDto);
    }


    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long appointmentId,
                                                            @RequestParam("date") LocalDateTime date,
                                                            @RequestParam("status") String status) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointment(appointmentId, date, status);
        return ResponseEntity.ok(updatedAppointment);
    }


    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{appointmentId}/products")
    public ResponseEntity<AppointmentDto> addProductToAppointment(@PathVariable Long appointmentId,
                                                                  @RequestParam("productId") Long productId) {
        AppointmentDto updatedAppointment = appointmentService.addProductToAppointment(appointmentId, productId);
        return ResponseEntity.ok(updatedAppointment);
    }


    @PostMapping("/{appointmentId}/treatments")
    public ResponseEntity<AppointmentDto> addTreatmentToAppointment(@PathVariable Long appointmentId,
                                                                    @RequestParam("treatmentId") Long treatmentId) {
        AppointmentDto updatedAppointment = appointmentService.addTreatmentToAppointment(appointmentId, treatmentId);
        return ResponseEntity.ok(updatedAppointment);
    }


    @PostMapping("/{appointmentId}/custom-treatment")
    public ResponseEntity<AppointmentDto> addCustomTreatmentToAppointment(@PathVariable Long appointmentId, @RequestParam double customPrice) {
        AppointmentDto updatedAppointment = appointmentService.addCustomTreatmentToAppointment(appointmentId, customPrice);
        return ResponseEntity.ok(updatedAppointment);
    }


    @PostMapping("/{appointmentId}/generate-receipt")
    public ResponseEntity<Receipt> generateReceipt(@PathVariable Long appointmentId) {
        Receipt receipt = appointmentService.generateReceipt(appointmentId);
        return ResponseEntity.ok(receipt);
    }

}
