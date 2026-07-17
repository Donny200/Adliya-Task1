package adliya.uz.task1.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import adliya.uz.task1.entity.Role;
import adliya.uz.task1.entity.User;
import adliya.uz.task1.repository.RoleRepository;
import adliya.uz.task1.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig da yaratilgan bo'lishi kerak

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Rollarni bazaga yozish (agar yo'q bo'lsa)
        List<String> roles = List.of(
                "ROLE_SUPER_ADMIN",
                "ROLE_ORG_ADMIN",
                "ROLE_MODERATOR",
                "ROLE_USER"
        );

        for (String roleName : roles) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println(roleName + " bazaga muvaffaqiyatli qo'shildi!");
            }
        }

        String adminEmail = "admin@reestr.uz"; // yoki usernam
        if (userRepository.findByEmail(adminEmail).isEmpty()) { // User repoda findByEmail yoki findByUsername bo'lishi kerak

            Role superAdminRole = roleRepository.findByName("ROLE_SUPER_ADMIN").orElseThrow();

            User superAdmin = new User();
            superAdmin.setEmail(adminEmail);
            superAdmin.setPassword(passwordEncoder.encode("admin123")); // Parolni hashlash
            superAdmin.setRole(superAdminRole);
            superAdmin.setFirstName("Super");
            superAdmin.setLastName("Admin");
            userRepository.save(superAdmin);
            System.out.println("Super Admin muvaffaqiyatli yaratildi! Email: admin@reestr.uz, Parol: admin123");
        }
    }
}