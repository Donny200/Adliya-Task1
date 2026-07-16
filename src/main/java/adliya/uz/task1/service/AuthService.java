package adliya.uz.task1.service;

import adliya.uz.task1.config.security.JwtService;
import adliya.uz.task1.dto.JwtResponse;
import adliya.uz.task1.dto.LoginRequest;
import adliya.uz.task1.dto.SignupRequest;
import adliya.uz.task1.entity.Role;
import adliya.uz.task1.entity.User;
import adliya.uz.task1.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtResponse signup(SignupRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("User with this email already exists: " + request.getEmail());
        }

        Role userRole = roleService.getByName("ROLE_USER");

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        user = userService.save(user);

        String token = jwtService.generateToken(user);
        return JwtResponse.builder().token(token).build();
    }

    public JwtResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userService.getByEmail(request.getEmail());
        String token = jwtService.generateToken(user);
        return JwtResponse.builder().token(token).build();
    }
}