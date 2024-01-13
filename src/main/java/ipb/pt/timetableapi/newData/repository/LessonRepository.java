package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}