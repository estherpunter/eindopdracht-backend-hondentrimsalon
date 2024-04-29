package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.*;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.AppointmentRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.CustomerRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.DogRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.TreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final DogRepository dogRepository;
    private final TreatmentRepository treatmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, CustomerRepository customerRepository, DogRepository dogRepository, TreatmentRepository treatmentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.dogRepository = dogRepository;
        this.treatmentRepository = treatmentRepository;
    }


    //Planning and scheduling appointments for customers

    //Adding treatments and products to appointments

    //Managing appointment status (e.g. confirming, rescheduling, canceling)

    //Retrieving appointment details


    public Appointment scheduleAppointment(Long customerId, Long dogId, LocalDateTime appointmentDate) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + customerId));

        Dog dog = dogRepository.findById(dogId)
                .orElseThrow(() -> new RecordNotFoundException("Dog not found with id: " + dogId));

        Appointment appointment = new Appointment();
        appointment.setCustomer(customer);
        appointment.setDog(dog);
        appointment.setDate(appointmentDate);

        // Perform any necessary business logic/validation before saving
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long appointmentId, LocalDateTime newDate, String newStatus) {
        //Check if the appointment with the given ID exists
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        //Update appointment properties
        appointment.setDate(newDate);
        appointment.setStatus(newStatus);

        //Save the updated appointment
        return appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long appointmentId) {
        //Retrieve appointment by ID
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        //Cancel the appointment
        appointmentRepository.delete(existingAppointment);
    }

    public void addProductToAppointment(Long appointmentId, Product product) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        appointment.getProducts().add(product);
        appointmentRepository.save(appointment);
    }

    public void addTreatmentToAppointment(Long appointmentId, Treatment treatment) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        appointment.getTreatments().add(treatment);
        appointmentRepository.save(appointment);
    }

    public Appointment addCustomTreatmentToAppointment(Long appointmentId, double customPrice) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        Treatment customTreatment = new Treatment();
        customTreatment.setName("Other");
        customTreatment.setPrice(customPrice);

        appointment.getTreatments().add(customTreatment);

        return appointmentRepository.save(appointment);
    }

}
