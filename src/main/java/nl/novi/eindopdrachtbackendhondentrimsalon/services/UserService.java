package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.RoleDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.UserDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.UsernameNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.RoleMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.UserMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Role;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.User;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.UserRepository;
import nl.novi.eindopdrachtbackendhondentrimsalon.utils.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleMapper roleMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
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
    return userMapper.userToUserDto(user);
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
        if (!userRepository.existsById(username)) {
            throw new UsernameNotFoundException(username);
        }
        User user = userRepository.findById(username).get();
        return roleMapper.rolesToRoleDtos(user.getRoles());
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
        return userMapper.userToUserDto(user);
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setApikey(userDto.getApikey());
        user.setEnabled(userDto.isEnabled());
        return user;
    }

}
