package nl.novi.eindopdrachtbackendhondentrimsalon.mappers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.TreatmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TreatmentMapper {

    TreatmentMapper INSTANCE = Mappers.getMapper(TreatmentMapper.class);

    @Mapping(target = "id", source = "treatment.id")
    @Mapping(target = "name", source = "treatment.name")
    @Mapping(target = "price", source = "treatment.price")
    TreatmentDto treatmentToTreatmentDto(Treatment treatment);
    Treatment treatmentDtoToTreatment(TreatmentDto treatmentDto);

    List<TreatmentDto> treatmentsToTreatmentDtos(List<Treatment> treatments);
}
