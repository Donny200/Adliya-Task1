package adliya.uz.task1.controller;

import adliya.uz.task1.config.security.CookieUtil;
import adliya.uz.task1.config.security.JwtService;
import adliya.uz.task1.dto.LoginRequest;
import adliya.uz.task1.dto.SignupRequest;
import adliya.uz.task1.dto.UserResponse;
import adliya.uz.task1.entity.User;
import adliya.uz.task1.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final CookieUtil cookieUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        User user = authService.signup(request);
        ResponseCookie cookie = cookieUtil.createAuthCookie(jwtService.generateToken(user));

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(UserResponse.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);
        ResponseCookie cookie = cookieUtil.createAuthCookie(jwtService.generateToken(user));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(UserResponse.from(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = cookieUtil.createLogoutCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}