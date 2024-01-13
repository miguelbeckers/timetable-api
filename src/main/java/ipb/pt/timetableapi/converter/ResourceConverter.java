package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.newData.model.Resource;
import ipb.pt.timetableapi.newData.repository.ResourceRepository;
import ipb.pt.timetableapi.oldData.model.Recurso;
import ipb.pt.timetableapi.oldData.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceConverter {
    @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    public void convert() {
        List<Recurso> recursos = recursoRepository.findAll();
        List<Resource> resources = new ArrayList<>();

        for (Recurso recurso : recursos) {
            Resource resource = new Resource();
            resource.setId((long) recurso.getId());
            resource.setName(recurso.getNome());
            resource.setAbbreviation(recurso.getAbrev());
            resources.add(resource);
        }

        resourceRepository.deleteAll();
        resourceRepository.saveAll(resources);
    }
}
