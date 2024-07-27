package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.constants.UserRole;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.RoleDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.UserDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable("username") String username) {
        UserDto userDto = userService.getUserByUsername(username);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        String newUsername = userService.createUser(userDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(newUsername)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("username") String username, @RequestBody UserDto userDto) {
        userService.updateUser(username, userDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/roles")
    public ResponseEntity<List<RoleDto>> getUserRoles(@PathVariable("username") String username) {
        List<RoleDto> roles = userService.getUserRoles(username);
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/{username}/roles")
    public ResponseEntity<Void> addUserRoles(@PathVariable("username") String username, @RequestBody UserRole role) {
        userService.addUserRoles(username, role);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{username}/roles/{role}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable("username") String username, @PathVariable("role") UserRole role) {
        userService.deleteUserRole(username, role);
        return ResponseEntity.noContent().build();
    }

}