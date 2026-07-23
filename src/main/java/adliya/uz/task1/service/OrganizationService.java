package adliya.uz.task1.service;

import adliya.uz.task1.dto.CreateOrganizationRequest;
import adliya.uz.task1.dto.UpdateOrganizationRequest;
import adliya.uz.task1.entity.Organization;
import adliya.uz.task1.exception.OrganizationAlreadyExistsException;
import adliya.uz.task1.exception.ResourceNotFoundException;
import adliya.uz.task1.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Organization update(Long id, UpdateOrganizationRequest request) {
        Organization org = getById(id);

        if (request.getName() != null && !request.getName().equals(org.getName())) {
            if (organizationRepository.existsByName(request.getName())) {
                throw new OrganizationAlreadyExistsException(
                        "Organization already exists with name: " + request.getName());
            }
            org.setName(request.getName());
        }

        if (request.getDescription() != null) {
            org.setDescription(request.getDescription());
        }

        return organizationRepository.save(org);
    }

    @Transactional
    public void deactivate(Long id) {
        Organization org = getById(id);
        org.setEnabled(false);
        organizationRepository.save(org);
    }
}

