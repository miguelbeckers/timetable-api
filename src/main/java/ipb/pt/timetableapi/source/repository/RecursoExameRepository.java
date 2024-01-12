package ipb.pt.timetableapi.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.source.model.RecursoExame;

@Repository
public interface RecursoExameRepository extends JpaRepository<RecursoExame, Integer> {
}
