package org.abraham.e_commerce_api.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.auth.JwtService;
import org.abraham.e_commerce_api.config.JwtConfig;
import org.abraham.e_commerce_api.dtos.*;
import org.abraham.e_commerce_api.repositories.UserRepository;
//import org.springframework.boot.web.server.Cookie;
import org.abraham.e_commerce_api.service.AuthService;
import org.abraham.e_commerce_api.service.PasswordResetService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final PasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
//        AUTHENTICATE USER BY FINDING A USER AND VALIDATION THEIR PASSWORD
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }

//        GENERATE TOKEN
        var user  = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

//        SEND REFRESH TOKEN VIA COOKIE
        var cookie = new Cookie("refresh_token",refreshToken.toString());
        cookie.setPath("/auth/refresh");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) throws BadRequestException, MessagingException {
        passwordResetService.sendResetPasswordEmail(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassWord(@Valid @RequestBody ResetPasswordRequest request, @RequestParam("token") String token) {
        passwordResetService.resetPassword(token,request.getNewPassword());
        return ResponseEntity.ok().build();
    }

//    HANDLE BAD REQUEST
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
