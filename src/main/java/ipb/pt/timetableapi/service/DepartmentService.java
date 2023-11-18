package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.Department;
import ipb.pt.timetableapi.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> findAll(){
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(Long id){
        return departmentRepository.findById(id);
    }

    public Department create(Department department){
        return departmentRepository.save(department);
    }

    public Department update(Department department){
        return departmentRepository.save(department);
    }

    public void delete(Department department){
        departmentRepository.delete(department);
    }
}
