package ipb.pt.timetableapi.optimizer.repository;

import ipb.pt.timetableapi.optimizer.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
