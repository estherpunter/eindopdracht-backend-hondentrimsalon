package nl.novi.eindopdrachtbackendhondentrimsalon.services;


import nl.novi.eindopdrachtbackendhondentrimsalon.exceptions.RecordNotFoundException;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Role;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.User;
import nl.novi.eindopdrachtbackendhondentrimsalon.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;


    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findById(username).orElseThrow(()->new RecordNotFoundException("Username not found"));

        String password = user.getPassword();

        Set<Role> roles = user.getRoles();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
    }


}
