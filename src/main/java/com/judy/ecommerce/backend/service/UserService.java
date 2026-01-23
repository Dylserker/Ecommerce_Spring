package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.dto.user.EditUserDTO;
import com.judy.ecommerce.backend.dto.user.EmailDTO;
import com.judy.ecommerce.backend.dto.user.UserDTO;
import com.judy.ecommerce.backend.entity.Users;
import com.judy.ecommerce.backend.exception.ConflictException;
import com.judy.ecommerce.backend.exception.InvalidFormatException;
import com.judy.ecommerce.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getSelfInfo(UserDetails userDetails) {
        // Shouldn't fail
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(userDetails.getUsername())
                .orElseThrow();

        return userToDTO(user);
    }

    public UserDTO editSelfInfo(UserDetails userDetails, EditUserDTO editUserDTO) {
        // Shouldn't fail
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(userDetails.getUsername())
                .orElseThrow();

        // If present, check firstname and lastname format, must be only letters and '-'
        if (editUserDTO.lastName().isPresent()) {
            if (!editUserDTO.lastName().get().matches("^[a-zA-Z-]+$")) {
                throw new InvalidFormatException("Name can only contain letters and '-'");
            }

            user.setLastname(editUserDTO.lastName().get());
        }

        if (editUserDTO.firstName().isPresent()) {
            if (!editUserDTO.firstName().get().matches("^[a-zA-Z-]+$")) {
                throw new InvalidFormatException("Name can only contain letters and '-'");
            }

            user.setFirstname(editUserDTO.firstName().get());
        }

        userRepository.save(user);

        return userToDTO(user);
    }

    public void editSelfEmail(UserDetails userDetails, EmailDTO emailDTO) {
        // Shouldn't fail
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(userDetails.getUsername())
                .orElseThrow();

        // Check email format, also check and disallow + aliases, + check uniqueness
        if (!emailDTO.email().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidFormatException("Invalid email format.");
        }

        if (userRepository.existsByEmailIgnoreCaseAndDisabledFalse(emailDTO.email())) {
            throw new ConflictException("There is already an account linked with this email address.");
        }

        user.setEmail(emailDTO.email());

        userRepository.save(user);
    }


    // UTILS //

    private UserDTO userToDTO(Users user) {
        return new UserDTO(
                user.getId(),
                user.getLastname(),
                user.getFirstname(),
                user.getEmail(),
                user.getRole().toString(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isDisabled()
        );
    }
}
