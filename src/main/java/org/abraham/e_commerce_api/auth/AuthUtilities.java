package org.abraham.e_commerce_api.auth;

import org.abraham.e_commerce_api.entities.User;
import org.abraham.e_commerce_api.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtilities {
    private final UserRepository userRepository;

    public AuthUtilities(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserAuthorities() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var authority =(GrantedAuthority) authentication.getAuthorities().toArray()[0];
        return authority.getAuthority().replace("ROLE_", "");
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var id = (Long) authentication.getPrincipal();
        return userRepository.findById(id).orElseThrow();
    }
}
