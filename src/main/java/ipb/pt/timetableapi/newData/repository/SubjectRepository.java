package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
