package nl.novi.eindopdrachtbackendhondentrimsalon.mappers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Appointment;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mappings({
            @Mapping(target = "customerId", source = "appointment.customer.id"),
            @Mapping(target = "dogId", source = "appointment.dog.id"),
            @Mapping(target = "productIds", expression = "java(mapProductsToIds(appointment.getProducts()))"),
            @Mapping(target = "treatmentIds", expression = "java(mapTreatmentsToIds(appointment.getTreatments()))")
    })

    AppointmentDto appointmentToAppointmentDto(Appointment appointment);

    Appointment appointmentDtoToAppointment(AppointmentDto appointmentDto);

    default List<Long> mapProductsToIds(List<Product> products) {
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
    }

    default List<Long> mapTreatmentsToIds(List<Treatment> treatments) {
        return treatments.stream()
                .map(Treatment::getId)
                .collect(Collectors.toList());
    }

}

