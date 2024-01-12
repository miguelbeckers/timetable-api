package ipb.pt.timetableapi.optimizer.repository;

import ipb.pt.timetableapi.optimizer.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
