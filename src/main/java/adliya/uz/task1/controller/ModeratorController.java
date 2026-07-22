package adliya.uz.task1.controller;

import adliya.uz.task1.dto.*;
import adliya.uz.task1.entity.User;
import adliya.uz.task1.service.ModeratorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/moderators")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN','ORG_ADMIN')")
public class ModeratorController {

    private final ModeratorService moderatorService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateModeratorRequest request) {
        User user = moderatorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @PostMapping("/promote")
    public ResponseEntity<UserResponse> promote(@Valid @RequestBody PromoteToModeratorRequest request) {
        User user = moderatorService.promote(request);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> moderators = moderatorService.getAll().stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(moderators);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponse.from(moderatorService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateModeratorRequest request) {
        return ResponseEntity.ok(UserResponse.from(moderatorService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        moderatorService.deactivate(id);
        return ResponseEntity.ok("Moderator deactivated successfully.");
    }
}
