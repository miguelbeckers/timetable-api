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

    public List<ResourceDto> findAll() {
        return resourceConverter.toDto(resourceRepository.findAll());
    }

    public ResourceDto findById(Long id) {
        return resourceConverter.toDto(resourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found")));
    }

    public ResourceDto create(ResourceDto resourceDto) {
        Resource resource = resourceConverter.toModel(resourceDto);
        return resourceConverter.toDto(resourceRepository.save(resource));
    }

    public ResourceDto update(ResourceDto resourceDto, Long id) {
        resourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        Resource resource = resourceConverter.toModel(resourceDto);
        return resourceConverter.toDto(resourceRepository.save(resource));
    }

    public void delete(Long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        resourceRepository.delete(resource);
    }

    public void deleteAll() {
        resourceRepository.deleteAll();
    }

    public List<ResourceDto> saveAll(List<ResourceDto> resourceDtos) {
        List<Resource> resources = resourceConverter.toModel(resourceDtos);
        return resourceConverter.toDto(resourceRepository.saveAll(resources));
    }
}

