package nl.novi.eindopdrachtbackendhondentrimsalon.dto;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Role;

import java.util.Set;

public class UserDto {

    public String username;
    public String password;
    public Set<Role> roles;

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
