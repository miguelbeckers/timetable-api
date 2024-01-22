package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.ResourceDto;
import ipb.pt.timetableapi.model.Resource;
import ipb.pt.timetableapi.repository.ResourceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }

    public Resource findById(Long id) {
        return resourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
    }

    public Resource create(ResourceDto resourceDto) {
        Resource resource = new Resource();
        BeanUtils.copyProperties(resourceDto, resource);
        return resourceRepository.save(resource);
    }

    public Resource update(ResourceDto resourceDto, Long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        BeanUtils.copyProperties(resourceDto, resource);
        return resourceRepository.save(resource);
    }

    public void delete(Long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        resourceRepository.delete(resource);
    }
}

