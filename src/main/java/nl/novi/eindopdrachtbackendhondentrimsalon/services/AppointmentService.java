package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Appointment;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment createAppointment(Appointment appointment) {
        // Perform any necessary business logic/validation before saving
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long appointmentId, Appointment appointment) {
        //Check if the appointment with the given ID exists
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        //Update appointment properties
        existingAppointment.setDate(appointment.getDate());
        existingAppointment.setProducts(appointment.getProducts());
        existingAppointment.setTreatments(appointment.getTreatments());

        //Save the updated appointment
        return appointmentRepository.save(existingAppointment);

    }

    public void cancelAppointment(Long appointmentId) {
        //Retrieve appointment by ID
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        //Cancel the appointment
        appointmentRepository.delete(existingAppointment);
    }



}
