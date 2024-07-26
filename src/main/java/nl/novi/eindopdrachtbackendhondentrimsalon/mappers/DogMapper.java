package nl.novi.eindopdrachtbackendhondentrimsalon.mappers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.DogDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DogMapper {

    DogMapper INSTANCE = Mappers.getMapper(DogMapper.class);

    @Mapping(target = "id", source = "dog.id")
    @Mapping(target = "name", source = "dog.name")
    @Mapping(target = "breed", source = "dog.breed")
    @Mapping(target = "age", source = "dog.age")


    DogDto dogToDogDto(Dog dog);

    Dog dogDtoToDog(DogDto dogDto);

}
