package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.ResourceConverter;
import ipb.pt.timetableapi.dto.ResourceDto;
import ipb.pt.timetableapi.model.Resource;
import ipb.pt.timetableapi.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final ResourceConverter resourceConverter;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository, ResourceConverter resourceConverter) {
        this.resourceRepository = resourceRepository;
        this.resourceConverter = resourceConverter;
    }

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    public Resource findById(Long id) {
        return resourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
    }

    public Resource create(ResourceDto resourceDto) {
        Resource resource = resourceConverter.toModel(resourceDto);
        return resourceRepository.save(resource);
    }

    public Resource update(ResourceDto resourceDto, Long id) {
        resourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        Resource resource = resourceConverter.toModel(resourceDto);
        return resourceRepository.save(resource);
    }

    public void delete(Long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        resourceRepository.delete(resource);
    }

    public void deleteAll() {
        resourceRepository.deleteAll();
    }

    public void saveAll(List<ResourceDto> resourceDtos) {
        List<Resource> resources = resourceConverter.toModel(resourceDtos);
        resourceRepository.saveAll(resources);
    }
}

