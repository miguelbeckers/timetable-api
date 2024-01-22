package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.DepartmentDto;
import ipb.pt.timetableapi.model.Department;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentConverter {
    public DepartmentDto toDto(Department department) {
        DepartmentDto departmentDto = new DepartmentDto();
        BeanUtils.copyProperties(department, departmentDto);
        return departmentDto;
    }

    public List<DepartmentDto> toDto(List<Department> departments) {
        List<DepartmentDto> departmentDtos = new ArrayList<>();
        for (Department department : departments) {
            departmentDtos.add(toDto(department));
        }

        return departmentDtos;
    }

    public Department toModel(DepartmentDto departmentDto) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentDto, department);
        return department;
    }

    public List<Department> toModel(List<DepartmentDto> departmentDtos) {
        List<Department> departments = new ArrayList<>();
        for (DepartmentDto departmentDto : departmentDtos) {
            departments.add(toModel(departmentDto));
        }

        return departments;
    }
}
