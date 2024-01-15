package ipb.pt.timetableapi.repository;

import ipb.pt.timetableapi.model.ClassroomResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomResourceRepository extends JpaRepository<ClassroomResource, Long> {
}
