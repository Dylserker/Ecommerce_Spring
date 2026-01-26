package com.judy.ecommerce.backend.controller.user;

import com.judy.ecommerce.backend.dto.user.EditUserDTO;
import com.judy.ecommerce.backend.dto.user.EmailDTO;
import com.judy.ecommerce.backend.dto.user.PasswordDTO;
import com.judy.ecommerce.backend.dto.user.UserDTO;
import com.judy.ecommerce.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/me")
public class UserAPIController {

    private final UserService userService;

    public UserAPIController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserDTO> getSelfInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getSelfInfo(userDetails));
    }

    @PatchMapping()
    public ResponseEntity<UserDTO> updateSelfInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody @Valid EditUserDTO editUserDTO) {
        return ResponseEntity.ok(userService.editSelfInfo(userDetails, editUserDTO));
    }

    @PatchMapping("/email")
    public ResponseEntity<String> updateSelfEmail(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody @Valid EmailDTO emailDTO) {
        userService.editSelfEmail(userDetails, emailDTO);
        return ResponseEntity.ok("Email changed.");
    }

    @PatchMapping("/password")
    public ResponseEntity<String> updateSelfPassword(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody @Valid PasswordDTO passwordDTO) {
        userService.editSelfPassword(userDetails, passwordDTO);
        return ResponseEntity.ok("Password changed.");
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteSelfUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteSelfUser(userDetails);
        return ResponseEntity.ok("Account deleted successfully.");
    }
}
