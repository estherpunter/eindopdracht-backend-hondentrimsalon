package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.RoleDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.UserDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.UsernameNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Role;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.User;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.UserRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.utils.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getUsers() {
        List<UserDto> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    public UserDto getUser(String username) {
        Optional<User> userOptional = userRepository.findById(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException(username));
    return fromUser(user);
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public String createUser(UserDto userDto) {
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userDto.setApikey(randomString);
        User newUser = userRepository.save(toUser(userDto));
        return newUser.getUsername();
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserDto newUser) {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException();
        User user = userRepository.findById(username).get();
        user.setPassword(newUser.getPassword());
        userRepository.save(user);
    }

    public Set<RoleDto> getRoles(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return user.getRoles().stream()
                .map(role -> new RoleDto(role.getUsername(), role.getRole()))
                .collect(Collectors.toSet());
    }

    public void addRole(String username, String role) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        user.addRole(new Role(username, role));
        userRepository.save(user);
    }

    public void removeRole(String username, String role) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Role roleToRemove = user.getRoles().stream()
                .filter(r -> r.getRole().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.removeRole(roleToRemove);
        userRepository.save(user);
    }

    public UserDto fromUser(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRoles(fromRoles(user.getRoles()));
        dto.setEnabled(user.isEnabled());
        dto.setApikey(user.getApikey());
        return dto;
    }


    public User toUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setApikey(userDto.getApikey());
        user.setEnabled(userDto.isEnabled());
        return user;
    }

    private Set<RoleDto> fromRoles(Set<Role> roles) {
        Set<RoleDto> roleDtos = new HashSet<>();
        for (Role role : roles) {
            roleDtos.add(new RoleDto(role.getUsername(), role.getRole()));
        }
        return roleDtos;
    }

}
