package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.constants.UserRole;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.RoleDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.UserDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.UsernameNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.RoleMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.UserMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Role;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.User;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.RoleRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, RoleMapper roleMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return userMapper.userToUserDto(user);
    }

    public String createUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User newUser = userRepository.save(userMapper.userDtoToUser(userDto));
        return newUser.getUsername();
    }

    public void updateUser(String username, UserDto userDto) {
        User userToUpdate = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(userToUpdate);
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public List<RoleDto> getUserRoles(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return user.getRoles().stream()
                .map(roleMapper::roleToRoleDto)
                .collect(Collectors.toList());
    }

    public void addUserRoles(String username, UserRole userRole) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Role role = new Role(user.getUsername(), userRole.name());
        user.addRole(role);
        userRepository.save(user);
    }

    public void deleteUserRole(String username, UserRole userRole) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        user.getRoles().removeIf(role -> role.getRole().equalsIgnoreCase(userRole.name()));
        userRepository.save(user);
    }

//    public UserDto fromUser(User user) {
//        return userMapper.userToUserDto(user);
//    }
//
//    public User toUser(UserDto userDto) {
//        User user = new User();
//        user.setUsername(userDto.getUsername());
//        user.setPassword(userDto.getPassword());
//        return user;
//    }

    public User toUser(UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        if (userDto.getRoles() != null) {
            Set<Role> roles = userDto.getRoles().stream()
                    .map(roleMapper::roleDtoToRole)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        return user;
    }

    public UserDto fromUser(User user) {
        UserDto userDto = userMapper.userToUserDto(user);
        if (user.getRoles() != null) {
            Set<RoleDto> roleDtos = user.getRoles().stream()
                    .map(roleMapper::roleToRoleDto)
                    .collect(Collectors.toSet());
            userDto.setRoles(roleDtos);
        }
        return userDto;
    }

}
