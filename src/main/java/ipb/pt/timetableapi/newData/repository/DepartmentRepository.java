package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
