package ipb.pt.timetableapi.repository;

import ipb.pt.timetableapi.model.SubjectCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectCourseRepository extends JpaRepository<SubjectCourse, Long> {
}
