package ipb.pt.timetableapi.optimizer.repository;

import ipb.pt.timetableapi.optimizer.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
