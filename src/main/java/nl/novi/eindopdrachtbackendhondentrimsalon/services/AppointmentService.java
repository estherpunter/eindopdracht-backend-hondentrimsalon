package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.*;
import nl.novi.eindopdrachtbackendhondentrimsalon.helpers.PriceCalculator;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.AppointmentMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.*;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final DogRepository dogRepository;
    private final TreatmentRepository treatmentRepository;
    private final ReceiptRepository receiptRepository;
    private final ProductRepository productRepository;
    private final AppointmentMapper appointmentMapper;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, CustomerRepository customerRepository, DogRepository dogRepository, TreatmentRepository treatmentRepository, ReceiptRepository receiptRepository, ProductRepository productRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.customerRepository = customerRepository;
        this.dogRepository = dogRepository;
        this.treatmentRepository = treatmentRepository;
        this.receiptRepository = receiptRepository;
        this.productRepository = productRepository;
        this.appointmentMapper = appointmentMapper;
    }

    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointmentMapper::appointmentToAppointmentDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));
        return appointmentMapper.appointmentToAppointmentDto(appointment);
    }

    public AppointmentDto scheduleAppointment(AppointmentDto appointmentDto) {
        Customer customer = customerRepository.findById(appointmentDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(appointmentDto.getCustomerId()));

        Dog dog = dogRepository.findById(appointmentDto.getDogId())
                .orElseThrow(() -> new DogNotFoundException(appointmentDto.getDogId()));

        Appointment appointment = appointmentMapper.appointmentDtoToAppointment(appointmentDto);
        appointment.setCustomer(customer);
        appointment.setDog(dog);

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.appointmentToAppointmentDto(savedAppointment);
    }

    public void updateAppointment(AppointmentDto appointmentDto) {
        Appointment appointment = appointmentRepository.findById(appointmentDto.getId())
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentDto.getId()));

        appointment.setDate(appointmentDto.getDate());
        appointment.setStatus(appointmentDto.getStatus());

        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        appointmentRepository.delete(appointment);
    }

    public void addProductToAppointment(Long appointmentId, Long productId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        appointment.getProducts().add(product);
        appointmentRepository.save(appointment);
    }

    public void addTreatmentToAppointment(Long appointmentId, Long treatmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Treatment treatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new TreatmentNotFoundException(treatmentId));

        appointment.getTreatments().add(treatment);
        appointmentRepository.save(appointment);
    }

    public Appointment addCustomTreatmentToAppointment(Long appointmentId, double customPrice) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Treatment customTreatment = new Treatment();
        customTreatment.setName("Other");
        customTreatment.setPrice(customPrice);

        appointment.getTreatments().add(customTreatment);

        return appointmentRepository.save(appointment);
    }

    public Receipt generateReceipt(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

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
