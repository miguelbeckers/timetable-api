package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.DepartmentConverter;
import ipb.pt.timetableapi.dto.DepartmentDto;
import ipb.pt.timetableapi.model.Department;
import ipb.pt.timetableapi.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentConverter departmentConverter;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, DepartmentConverter departmentConverter) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));
    }

    public Department create(DepartmentDto departmentDto) {
        Department department = departmentConverter.toModel(departmentDto);
        return departmentRepository.save(department);
    }

    public Department update(DepartmentDto departmentDto, Long id) {
         departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        Department department = departmentConverter.toModel(departmentDto);
        return departmentRepository.save(department);
    }

    public void delete(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        departmentRepository.delete(department);
    }

    public void deleteAll() {
        departmentRepository.deleteAll();
    }

    public void saveAll(List<DepartmentDto> departmentDtos) {
        List<Department> departments = departmentConverter.toModel(departmentDtos);
        departmentRepository.saveAll(departments);
    }
}

