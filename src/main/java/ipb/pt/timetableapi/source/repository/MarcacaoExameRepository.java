package ipb.pt.timetableapi.source.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.source.model.MarcacaoExame;

@Repository
public interface MarcacaoExameRepository extends JpaRepository<MarcacaoExame, Integer> {
}
