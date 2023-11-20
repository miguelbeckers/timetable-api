package ipb.pt.timetableapi.repository;

import ipb.pt.timetableapi.model.LessonStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonStudentRepository extends JpaRepository<LessonStudent, Long> {
}
