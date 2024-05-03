package nl.novi.eindopdrachtbackendhondentrimsalon.mappers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.RoleDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDto roleToRoleDto(Role role);

    Set<RoleDto> rolesToRoleDtos(Set<Role> roles);
}

