package ipb.pt.timetableapi.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.source.model.Aula;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {
}
