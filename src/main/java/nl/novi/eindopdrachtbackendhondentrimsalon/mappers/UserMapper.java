package nl.novi.eindopdrachtbackendhondentrimsalon.mappers;


import nl.novi.eindopdrachtbackendhondentrimsalon.dto.UserDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "roles", source = "user.roles") // Map roles using RoleMapper
    })
    UserDto userToUserDto(User user);

}
