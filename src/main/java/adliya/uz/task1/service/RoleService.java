package adliya.uz.task1.service;

import adliya.uz.task1.entity.Role;
import adliya.uz.task1.exception.ResourceNotFoundException;
import adliya.uz.task1.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name));
    }
}