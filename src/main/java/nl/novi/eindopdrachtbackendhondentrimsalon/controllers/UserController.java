package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AuthenticationRequest;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.RoleDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.UserDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable("username") String username) {
        UserDto userDto = userService.getUserByUsername(username);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody AuthenticationRequest authenticationRequest) {
        String newUsername = userService.createUser(authenticationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(newUsername)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUserPassword(@PathVariable("username") String username,
                                                      @RequestBody Map<String, String> passwordUpdate) {
        if (!passwordUpdate.containsKey("password")) {
            throw new IllegalArgumentException("Request body must contain a password field");
        }
        String newPassword = passwordUpdate.get("password");
        UserDto updatedUserDto = userService.updateUserPassword(username, newPassword);
        return ResponseEntity.ok(updatedUserDto);
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
    public ResponseEntity<List<RoleDto>> addUserRole(@PathVariable("username") String username,
                                                     @RequestParam("role") String role) {
        List<RoleDto> updatedRoles = userService.addUserRole(username, role);
        return ResponseEntity.ok(updatedRoles);
    }

    @DeleteMapping("/{username}/roles/{role}")
    public ResponseEntity<UserDto> deleteUserRole(@PathVariable("username") String username,
                                                  @PathVariable("role") String role) {
        UserDto updatedUserDto = userService.deleteUserRole(username, role);
        return ResponseEntity.ok(updatedUserDto);
    }
}