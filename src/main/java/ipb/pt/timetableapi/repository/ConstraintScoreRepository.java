package ipb.pt.timetableapi.repository;

import ipb.pt.timetableapi.model.ConstraintScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstraintScoreRepository extends JpaRepository<ConstraintScore, Long> {
}
