package com.judy.ecommerce.backend.controller.admin;

import com.judy.ecommerce.backend.RoleEnum;
import com.judy.ecommerce.backend.dto.user.UserDTO;
import com.judy.ecommerce.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsersAdmin() {
        return ResponseEntity.ok(userService.getAllUsersAdmin());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserByIdAdmin(@PathVariable long id) {
        return ResponseEntity.ok(userService.getUserByIdAdmin(id));
    }

    @PatchMapping("/{id}/admin")
    public ResponseEntity<UserDTO> setUserToAdmin(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable long id) {
        return  ResponseEntity.ok(userService.editRoleAdmin(userDetails, id, RoleEnum.ADMIN));
    }

    @PatchMapping("/{id}/user")
    public ResponseEntity<UserDTO> setAdminToUser(@AuthenticationPrincipal UserDetails userDetails,
                                                  @PathVariable long id) {
        return  ResponseEntity.ok(userService.editRoleAdmin(userDetails, id, RoleEnum.USER));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserByIdAdmin(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable long id) {
        userService.deleteUserByIdAdmin(userDetails, id);
        return ResponseEntity.ok("Account disabled successfully.");
    }
}
