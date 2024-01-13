package ipb.pt.timetableapi.newData.service;

import ipb.pt.timetableapi.newData.model.Resource;
import ipb.pt.timetableapi.newData.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    public List<Resource> findAll(){
        return resourceRepository.findAll();
    }

    public Optional<Resource> findById(Long id){
        return resourceRepository.findById(id);
    }

    public Resource create(Resource resource){
        return resourceRepository.save(resource);
    }

    public Resource update(Resource resource){
        return resourceRepository.save(resource);
    }

    public void delete(Resource resource){
        resourceRepository.delete(resource);
    }
}
