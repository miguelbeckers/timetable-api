package ipb.pt.timetableapi.repository;

import ipb.pt.timetableapi.model.LessonUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonUnitRepository extends JpaRepository<LessonUnit, Long> {
    List<LessonUnit> findByLessonIsEnabledTrue();
}
