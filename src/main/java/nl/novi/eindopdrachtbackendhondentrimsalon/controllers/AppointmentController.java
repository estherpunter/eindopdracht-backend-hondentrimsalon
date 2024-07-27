package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.ScheduleAppointmentRequest;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.UpdateAppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Receipt;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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


    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointmentDtos = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointmentDtos);
    }


    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable("appointmentId") Long appointmentId) {
        AppointmentDto appointmentDto = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointmentDto);
    }


    @PostMapping
    public ResponseEntity<Void> scheduleAppointment(@RequestBody ScheduleAppointmentRequest request) {
        AppointmentDto scheduledAppointmentDto = appointmentService.scheduleAppointment(request);

        Long appointmentId = scheduledAppointmentDto.getId();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(appointmentId)
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long appointmentId,
                                                            @RequestBody UpdateAppointmentDto updateAppointmentDto) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointment(appointmentId, updateAppointmentDto);
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
