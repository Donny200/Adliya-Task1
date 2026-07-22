package adliya.uz.task1.controller;

import adliya.uz.task1.dto.*;
import adliya.uz.task1.entity.User;
import adliya.uz.task1.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/org-admins")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    public ResponseEntity<UserResponse> createOrgAdmin(@Valid @RequestBody CreateOrgAdminRequest request) {
        User user = adminUserService.createOrgAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @PostMapping("/promote")
    public ResponseEntity<UserResponse> promote(@Valid @RequestBody PromoteToOrgAdminRequest request) {
        User user = adminUserService.promoteToOrgAdmin(request);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> orgAdmins = adminUserService.getAllOrgAdmins().stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(orgAdmins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.from(adminUserService.getOrgAdminById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateOrgAdminRequest request) {
        return ResponseEntity.ok(UserResponse.from(adminUserService.updateOrgAdmin(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        adminUserService.deactivateOrgAdmin(id);
        return ResponseEntity.ok("Org admin deactivated successfully.");
    }
}

