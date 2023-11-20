package ipb.pt.timetableapi.repository;

import ipb.pt.timetableapi.model.LessonResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonResourceRepository extends JpaRepository<LessonResource, Long> {
}
