package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.ClassroomResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomResourceRepository extends JpaRepository<ClassroomResource, Long> {
}
