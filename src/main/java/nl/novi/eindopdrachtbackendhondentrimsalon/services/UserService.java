package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import jakarta.validation.ValidationException;
import nl.novi.eindopdrachtbackendhondentrimsalon.constants.UserRole;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AuthenticationRequest;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.RoleDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.UserDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.InvalidRoleException;
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

    public String createUser(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest.getUsername() == null || authenticationRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (authenticationRequest.getPassword() == null || authenticationRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        UserDto userDto = new UserDto();
        userDto.setUsername(authenticationRequest.getUsername());
        userDto.setPassword(authenticationRequest.getPassword());
        
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User newUser = userMapper.userDtoToUser(userDto);
        newUser = userRepository.save(newUser);

        return newUser.getUsername();
    }

    public UserDto updateUserPassword(String username, String password) {
        User userToUpdate = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        if (password != null && !password.isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(password));
        } else {
            throw new ValidationException("Password cannot be null or empty.");
        }

        User updatedUser = userRepository.save(userToUpdate);

        return userMapper.userToUserDto(updatedUser);
    }

    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException(username);
        }
        userRepository.deleteById(username);
    }

    public List<RoleDto> getUserRoles(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return user.getRoles().stream()
                .map(roleMapper::roleToRoleDto)
                .collect(Collectors.toList());
    }

    public List<RoleDto> addUserRole(String username, String roleName) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        try {
            UserRole userRole = UserRole.valueOf(roleName.toUpperCase());
            Role role = new Role(user.getUsername(), userRole.name());
            user.addRole(role);
            userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role: " + roleName);
        }

        return user.getRoles().stream()
                .map(roleMapper::roleToRoleDto)
                .collect(Collectors.toList());
    }

    public UserDto deleteUserRole(String username, String roleName) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        boolean roleRemoved = user.getRoles().removeIf(role -> role.getRole().equalsIgnoreCase(roleName));

        if (!roleRemoved) {
            throw new InvalidRoleException("Role: " + roleName + " not found for user: " + username);
        }
        User updatedUser = userRepository.save(user);

        return userMapper.userToUserDto(updatedUser);
    }

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
