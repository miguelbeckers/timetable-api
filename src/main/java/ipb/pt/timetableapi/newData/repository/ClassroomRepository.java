package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
