package ipb.pt.timetableapi.repository;

import ipb.pt.timetableapi.model.LessonUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonUnitRepository extends JpaRepository<LessonUnit, Long> {
}
