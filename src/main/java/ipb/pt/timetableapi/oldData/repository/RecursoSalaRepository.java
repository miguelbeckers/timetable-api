package ipb.pt.timetableapi.oldData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.oldData.model.RecursoSala;

import java.util.List;

@Repository
public interface RecursoSalaRepository extends JpaRepository<RecursoSala, Integer> {
    List<RecursoSala> findByIdRec(int idRec);
}
