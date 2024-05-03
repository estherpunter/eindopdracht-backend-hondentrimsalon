package nl.novi.eindopdrachtbackendhondentrimsalon.services;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.RoleDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.UserDto;
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

    public String createUser(UserDto userDto) {
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userDto.setApikey(randomString);
        User newUser = userRepository.save(toUser(userDto));
        return newUser.getUsername();
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserDto newUserDto) {
        User userToUpdate = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        userToUpdate.setPassword(newUserDto.getPassword());
        userRepository.save(userToUpdate);
    }

    public Set<RoleDto> getRoles(String username) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return roleMapper.rolesToRoleDtos(user.getRoles());
    }

    public void addRole(String username, String roleName) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Role newRole = new Role(username, roleName);
        user.addRole(newRole);
        userRepository.save(user);
    }

    public void removeRole(String username, String roleName) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Optional<Role> roleToRemove = user.getRoles().stream()
                .filter(role -> role.getRole().equalsIgnoreCase(roleName))
                .findFirst();
        roleToRemove.ifPresent(user::removeRole);
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
