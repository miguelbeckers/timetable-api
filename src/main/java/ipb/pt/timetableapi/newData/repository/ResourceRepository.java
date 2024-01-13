package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
