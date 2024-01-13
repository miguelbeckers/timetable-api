package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.newData.model.Department;
import ipb.pt.timetableapi.newData.repository.DepartmentRepository;
import ipb.pt.timetableapi.oldData.model.Departamento;
import ipb.pt.timetableapi.oldData.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartmentConverter {
    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public void convert() {
        List<Departamento> departamentos = departamentoRepository.findAll();
        List<Department> departments = departmentRepository.findAll();

        for (Departamento departamento : departamentos) {
            Department department = new Department();
            department.setId((long) departamento.getId());
            department.setName(departamento.getNome());
            department.setAbbreviation(departamento.getAbrev());

            department.setIpbCodEscola(departamento.getIpbCodEscola());
            department.setIpbEmpCcusto(departamento.getIpbEmpCcusto());
            departments.add(department);
        }

        departmentRepository.deleteAll();
        departmentRepository.saveAll(departments);
    }
}
