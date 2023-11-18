package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.ResourceType;
import ipb.pt.timetableapi.repository.ResourceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceTypeService {
    @Autowired
    private ResourceTypeRepository resourceTypeRepository;

    public List<ResourceType> findAll(){
        return resourceTypeRepository.findAll();
    }

    public Optional<ResourceType> findById(Long id){
        return resourceTypeRepository.findById(id);
    }

    public ResourceType create(ResourceType resourceType){
        return resourceTypeRepository.save(resourceType);
    }

    public ResourceType update(ResourceType resourceType){
        return resourceTypeRepository.save(resourceType);
    }

    public void delete(ResourceType resourceType){
        resourceTypeRepository.delete(resourceType);
    }
}
