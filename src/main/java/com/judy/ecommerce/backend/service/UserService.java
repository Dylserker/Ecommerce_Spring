package com.judy.ecommerce.backend.service;

import com.judy.ecommerce.backend.RoleEnum;
import com.judy.ecommerce.backend.dto.user.EditUserDTO;
import com.judy.ecommerce.backend.dto.user.PasswordDTO;
import com.judy.ecommerce.backend.dto.user.UserDTO;
import com.judy.ecommerce.backend.entity.Users;
import com.judy.ecommerce.backend.exception.*;
import com.judy.ecommerce.backend.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        // Also check for size <= 64 (regex already checks for >= 1)
        if (editUserDTO.lastName().isPresent()) {
            String lastName = editUserDTO.lastName().get();
            if (!lastName.matches("^[a-zA-Z-]+$")
                || lastName.length() > 64) {
                throw new InvalidFormatException(
                        "Name can only contain letters and '-', must not be empty and cannot exceed 64 characters long"
                );
            }

            user.setLastname(lastName);
        }

        if (editUserDTO.firstName().isPresent()) {
            String firstName = editUserDTO.firstName().get();
            if (!firstName.matches("^[a-zA-Z-]+$")
                    || firstName.length() > 64) {
                throw new InvalidFormatException(
                        "Name can only contain letters and '-', must not be empty and cannot exceed 64 characters long"
                );
            }

            user.setFirstname(firstName);
        }

        if (editUserDTO.email().isPresent()) {
            String email = editUserDTO.email().get();

            // Check email size
            if (email.length() > 255) {
                throw new InvalidFormatException("Email cannot exceed 255 characters.");
            }

            // Check email format, also check and disallow + aliases, + check uniqueness
            if (email.matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                throw new InvalidFormatException("Invalid email format.");
            }

            if (userRepository.existsByEmailIgnoreCaseAndDisabledFalse(email)) {
                throw new ConflictException("There is already an account linked with this email address.");
            }

            user.setEmail(email);
        }

        userRepository.save(user);

        return userToDTO(user);
    }

    public void editSelfPassword(UserDetails userDetails, PasswordDTO passwordDTO) {
        // Shouldn't fail
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(userDetails.getUsername())
                .orElseThrow();

        if (!passwordEncoder.matches(passwordDTO.currentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password.");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.newPassword()));
        userRepository.save(user);
    }

    public void deleteSelfUser(UserDetails userDetails) {
        // Shouldn't fail
        Users user = userRepository.findByEmailIgnoreCaseAndDisabledFalse(userDetails.getUsername())
                .orElseThrow();

        // Only if the user is an admin
        if (user.getRole() == RoleEnum.ADMIN) {
            // Make sure the user table contains at least another admin to avoid being locked out
            if (userRepository.countUsersByRoleIs(RoleEnum.ADMIN) < 2) {
                throw new ConflictException("Cannot disable user, there must be at least 1 active admin.");
            }
        }

        user.setDisabled(true);
        userRepository.save(user);
    }


    // ADMIN //

    public List<UserDTO> getAllUsersAdmin() {
        List<Users> users = userRepository.findAll();

        return listToDTO(users);
    }

    public UserDTO getUserByIdAdmin(long id) {
        // Ignore disabled check
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userToDTO(user);
    }

    public UserDTO editRoleAdmin(UserDetails userDetails, long id, RoleEnum role) {
        // Shouldn't fail
        Users selfUser = userRepository.findByEmailIgnoreCaseAndDisabledFalse(userDetails.getUsername())
                .orElseThrow();

        // Do not change your own role
        if (selfUser.getId() == id) {
            throw new ForbiddenException("Cannot change your own role.");
        }

        // Get the account to change
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.setRole(role);
        userRepository.save(user);

        return userToDTO(user);
    }

    public void deleteUserByIdAdmin(UserDetails userDetails, long id) {
        // Shouldn't fail
        Users selfUser = userRepository.findByEmailIgnoreCaseAndDisabledFalse(userDetails.getUsername())
                .orElseThrow();

        // Do not disable its own account this way
        if (selfUser.getId() == id) {
            throw new BadRequestException("Cannot disable your account this way.");
        }

        // Get the account to disable
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.setDisabled(true);
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

    private List<UserDTO> listToDTO(List<Users> listUsers) {
        List<UserDTO> listDTO = new ArrayList<>();

        for (Users user : listUsers) {
            listDTO.add(userToDTO(user));
        }

        return listDTO;
    }
}
