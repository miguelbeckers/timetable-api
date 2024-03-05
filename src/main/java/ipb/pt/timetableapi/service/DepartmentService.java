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

    public List<DepartmentDto> findAll() {
        return departmentConverter.toDto(departmentRepository.findAll());
    }

    public DepartmentDto findById(Long id) {
        return departmentConverter.toDto(departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")));
    }

    public DepartmentDto create(DepartmentDto departmentDto) {
        Department department = departmentConverter.toModel(departmentDto);
        return departmentConverter.toDto(departmentRepository.save(department));
    }

    public DepartmentDto update(DepartmentDto departmentDto, Long id) {
         departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        Department department = departmentConverter.toModel(departmentDto);
        return departmentConverter.toDto(departmentRepository.save(department));
    }

    public void delete(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        departmentRepository.delete(department);
    }

    public void deleteAll() {
        departmentRepository.deleteAll();
    }

    public List<DepartmentDto> saveAll(List<DepartmentDto> departmentDtos) {
        List<Department> departments = departmentConverter.toModel(departmentDtos);
        return departmentConverter.toDto(departmentRepository.saveAll(departments));
    }
}

