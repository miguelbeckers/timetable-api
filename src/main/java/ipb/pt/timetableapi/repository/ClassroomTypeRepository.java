package ipb.pt.timetableapi.repository;

import ipb.pt.timetableapi.model.ClassroomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomTypeRepository extends JpaRepository<ClassroomType, Long> {
}
