package nl.novi.eindopdrachtbackendhondentrimsalon.mappers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(target = "customerId", source = "appointment.customer.id")
    @Mapping(target = "dogId", source = "appointment.dog.id")
    @Mapping(target = "productIds", source = "appointment.products")
    @Mapping(target = "treatmentIds", source = "appointment.treatments")
    AppointmentDto appointmentToAppointmentDto(Appointment appointment);

    Appointment appointmentDtoToAppointment(AppointmentDto appointmentDto);
}

