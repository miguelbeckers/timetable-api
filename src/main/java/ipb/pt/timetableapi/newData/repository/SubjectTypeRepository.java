package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.SubjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectTypeRepository extends JpaRepository<SubjectType, Long> {
}
