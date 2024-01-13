package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
