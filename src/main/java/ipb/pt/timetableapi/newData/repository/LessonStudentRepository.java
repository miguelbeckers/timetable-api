package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.LessonStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonStudentRepository extends JpaRepository<LessonStudent, Long> {
}
