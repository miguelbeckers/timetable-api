package ipb.pt.timetableapi.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.source.model.Exame;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Integer> {
}
