package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.ClassroomResourceDto;
import ipb.pt.timetableapi.model.ClassroomResource;
import ipb.pt.timetableapi.repository.ClassroomResourceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClassroomResourceService {
    @Autowired
    private ClassroomResourceRepository classroomResourceRepository;

    public List<ClassroomResource> findAll() {
        return classroomResourceRepository.findAll();
    }

    public ClassroomResource findById(Long id) {
        return classroomResourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomResource not found"));
    }

    public ClassroomResource create(ClassroomResourceDto classroomResourceDto) {
        ClassroomResource classroomResource = new ClassroomResource();
        BeanUtils.copyProperties(classroomResourceDto, classroomResource);
        return classroomResourceRepository.save(classroomResource);
    }

    public ClassroomResource update(ClassroomResourceDto classroomResourceDto, Long id) {
        ClassroomResource classroomResource = classroomResourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomResource not found"));

        BeanUtils.copyProperties(classroomResourceDto, classroomResource);
        return classroomResourceRepository.save(classroomResource);
    }

    public void delete(Long id) {
        ClassroomResource classroomResource = classroomResourceRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ClassroomResource not found"));

        classroomResourceRepository.delete(classroomResource);
    }

    public void deleteAll() {
        classroomResourceRepository.deleteAll();
    }

    public void createMany(List<ClassroomResourceDto> classroomResourceDtos) {
        List<ClassroomResource> classroomResources = new ArrayList<>();

        for (ClassroomResourceDto classroomResourceDto : classroomResourceDtos) {
            ClassroomResource classroomResource = new ClassroomResource();
            BeanUtils.copyProperties(classroomResourceDto, classroomResource);
            classroomResources.add(classroomResource);
        }

        classroomResourceRepository.saveAll(classroomResources);
    }
}

