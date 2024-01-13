package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
