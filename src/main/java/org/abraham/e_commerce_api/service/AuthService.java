package org.abraham.e_commerce_api.service;

import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.auth.AuthUtilities;
import org.abraham.e_commerce_api.exceptions.PermissionDeniedException;
import org.abraham.e_commerce_api.repositories.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthUtilities authUtilities;
    private final UserRepository userRepository;

    public void forgotPassword(String email) throws BadRequestException {
       var user = userRepository.findByEmail(email).orElse(null);
       if (user == null) {
           throw new BadRequestException("User with the email"+email+" Not found");
       }

    }
}
