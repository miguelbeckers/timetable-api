package ipb.pt.timetableapi.optimizer.repository;

import ipb.pt.timetableapi.optimizer.model.SubjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectTypeRepository extends JpaRepository<SubjectType, Long> {
}
