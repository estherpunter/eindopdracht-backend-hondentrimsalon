package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import jakarta.validation.ValidationException;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentScheduleRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.*;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.AppointmentMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.*;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public AppointmentDto scheduleAppointment(AppointmentScheduleRequestDto request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        Dog dog = dogRepository.findById(request.getDogId())
                .orElseThrow(() -> new DogNotFoundException(request.getDogId()));

        if (request.getDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Appointment date cannot be in the past.");
        }

        boolean hasOverlap = appointmentRepository.findById(customer.getId()).stream()
                .anyMatch(appointment -> appointment.getDate().isEqual(request.getDate()));
        if (hasOverlap) {
            throw new ValidationException("Customer already has an appointment at this date and time.");
        }

        Appointment appointment = new Appointment();
        appointment.setDate(request.getDate());
        appointment.setCustomer(customer);
        appointment.setDog(dog);
        appointment.setStatus("Scheduled");
        appointment.setProducts(new ArrayList<>());
        appointment.setTreatments(new ArrayList<>());

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToAppointmentDto(savedAppointment);
    }

    public AppointmentDto updateAppointment(Long appointmentId, AppointmentRequestDto appointmentRequestDto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        if (appointmentRequestDto.getDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Appointment date cannot be in the past.");
        }

        appointment.setDate(appointmentRequestDto.getDate());
        appointment.setStatus(appointmentRequestDto.getStatus());

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToAppointmentDto(updatedAppointment);
    }

    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));
        appointmentRepository.delete(appointment);
    }

    public AppointmentDto addProductToAppointment(Long appointmentId, Long productId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        appointment.getProducts().add(product);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToAppointmentDto(updatedAppointment);
    }

    public AppointmentDto addTreatmentToAppointment(Long appointmentId, Long treatmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Treatment treatment = treatmentRepository.findById(treatmentId)
                .orElseThrow(() -> new TreatmentNotFoundException(treatmentId));

        appointment.getTreatments().add(treatment);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToAppointmentDto(updatedAppointment);
    }

    public AppointmentDto addCustomTreatmentToAppointment(Long appointmentId, double customPrice) {
        if (customPrice <= 0) {
            throw new ValidationException("Custom treatment price must be positive.");
        }

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Treatment customTreatment = new Treatment();
        customTreatment.setName("Other");
        customTreatment.setPrice(customPrice);
        treatmentRepository.save(customTreatment);

        appointment.getTreatments().add(customTreatment);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        return appointmentMapper.appointmentToAppointmentDto(updatedAppointment);
    }

    public Receipt generateReceipt(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Receipt receipt = new Receipt();
        receipt.setCustomerId(appointment.getCustomer().getId());
        receipt.setCustomerName(appointment.getCustomer().getName());
        receipt.setDogId(appointment.getDog().getId());
        receipt.setDogName(appointment.getDog().getName());
        receipt.setStatus("Completed");

        List<Product> products = new ArrayList<>(appointment.getProducts());
        List<Treatment> treatments = new ArrayList<>(appointment.getTreatments());

        receipt.setProducts(products);
        receipt.setTreatments(treatments);

        double totalPrice = PriceCalculator.calculateTotalPrice(products, treatments);
        receipt.setTotalPrice(totalPrice);

        return receiptRepository.save(receipt);
    }

    private static class PriceCalculator {

        public static double calculateTotalPrice(List<Product> products, List<Treatment> treatments) {
            double totalPrice = 0.0;

            for (Product product : products) {
                totalPrice += product.getPrice();
            }

            for (Treatment treatment : treatments) {
                totalPrice += treatment.getPrice();
            }

            return totalPrice;
        }
    }
}
