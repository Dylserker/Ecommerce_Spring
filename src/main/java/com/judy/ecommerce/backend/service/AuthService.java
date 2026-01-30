package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.RoleEnum;
import com.judy.ecommerce.backend.dto.auth.AuthDTO;
import com.judy.ecommerce.backend.dto.auth.LoginDTO;
import com.judy.ecommerce.backend.dto.auth.RegisterDTO;
import com.judy.ecommerce.backend.exception.ConflictException;
import com.judy.ecommerce.backend.exception.InvalidFormatException;
import com.judy.ecommerce.backend.entity.Users;
import com.judy.ecommerce.backend.repository.UserRepository;
import com.judy.ecommerce.backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthDTO register(RegisterDTO request) {
        // Check firstname and lastname format, must be only letters and '-'
        if (!request.lastName().matches("^[a-zA-Z-]+$") || !request.firstName().matches("^[a-zA-Z-]+$")) {
            throw new InvalidFormatException("Name can only contain letters and '-'");
        }

        // Check email format, also check and disallow + aliases
        if (!request.email().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidFormatException("Invalid email format.");
        }

        // Check email uniqueness
        if (userRepository.existsByEmailIgnoreCaseAndDisabledFalse(request.email())) {
            throw new ConflictException("There is already an account linked with this email address.");
        }

        // Check password format
        if (!request.password().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]+$")) {
            throw new InvalidFormatException("Invalid password format.");
        }

        // Create the user object
        Users user = new Users();

        // Set the user properties
        user.setLastname(request.lastName());
        user.setFirstname(request.firstName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setDisabled(false);
        user.setRole(RoleEnum.USER);

        // Insert the user in database
        userRepository.save(user);

        // Return the JWT token
        String token = jwtService.generateToken(user);
        Date expiration = jwtService.extractExpiration(token);
        return new AuthDTO(
                token,
                expiration
        );
    }

    public AuthDTO login(LoginDTO request) {
        // Authentication
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password()
                )
        );

        // Get the user
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        // Return the JWT token
        String token = jwtService.generateToken(user);
        Date expiration = jwtService.extractExpiration(token);
        return new AuthDTO(
                token,
                expiration
        );
    }
}
