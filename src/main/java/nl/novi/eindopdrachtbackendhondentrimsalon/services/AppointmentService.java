package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.helpers.PriceCalculator;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.*;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final DogRepository dogRepository;
    private final TreatmentRepository treatmentRepository;
    private final ReceiptRepository receiptRepository;

    private final ProductRepository productRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, CustomerRepository customerRepository, DogRepository dogRepository, TreatmentRepository treatmentRepository, ReceiptRepository receiptRepository, ProductRepository productRepository) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.dogRepository = dogRepository;
        this.treatmentRepository = treatmentRepository;
        this.receiptRepository = receiptRepository;
        this.productRepository = productRepository;
    }


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

    public void updateAppointment(Long appointmentId, LocalDateTime newDate, String newStatus) {
        //Check if the appointment with the given ID exists
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        //Update appointment properties
        appointment.setDate(newDate);
        appointment.setStatus(newStatus);

        //Save the updated appointment
        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long appointmentId) {
        //Retrieve appointment by ID
        Appointment existingAppointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        //Cancel the appointment
        appointmentRepository.delete(existingAppointment);
    }

    public void addProductToAppointment(Long appointmentId, Long productId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Product not found with id: " + productId));

        appointment.getProducts().add(product);
        appointmentRepository.save(appointment);
    }

    public void addTreatmentToAppointment(Long appointmentId, Long treatmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        Treatment treatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new RecordNotFoundException("Treatment not found with id: " + treatmentId));

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

    public Receipt generateReceipt(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RecordNotFoundException("Appointment not found with id: " + appointmentId));

        Receipt receipt = new Receipt();

        List<Product> products = appointment.getProducts();
        List<Treatment> treatments = appointment.getTreatments();

        receipt.setProducts(products);
        receipt.setTreatments(treatments);

        double totalPrice = PriceCalculator.calculateTotalPrice(products, treatments);
        receipt.setTotalPrice(totalPrice);

        return receiptRepository.save(receipt);
    }
}
