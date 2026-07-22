package adliya.uz.task1.service;

import adliya.uz.task1.dto.CreateOrganizationRequest;
import adliya.uz.task1.entity.Organization;
import adliya.uz.task1.exception.OrganizationAlreadyExistsException;
import adliya.uz.task1.exception.ResourceNotFoundException;
import adliya.uz.task1.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public Organization create(CreateOrganizationRequest request) {
        if (organizationRepository.existsByName(request.getName())) {
            throw new OrganizationAlreadyExistsException(
                    "Organization already exists with name: " + request.getName());
        }

        Organization org = Organization.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return organizationRepository.save(org);
    }

    public Organization getById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found, ID: " + id));
    }

    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }
}
