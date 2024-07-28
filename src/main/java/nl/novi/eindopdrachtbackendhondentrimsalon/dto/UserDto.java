package nl.novi.eindopdrachtbackendhondentrimsalon.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class UserDto {

    @NotBlank
    public String username;

    @NotBlank
    public String password;

    public Set<RoleDto> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }
}
