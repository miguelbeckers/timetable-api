package ipb.pt.timetableapi.oldData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.oldData.model.AulaDocente;

import java.util.List;

@Repository
public interface AulaDocenteRepository extends JpaRepository<AulaDocente, Integer> {
    List<AulaDocente> findByIdDocente(Integer docenteId);
    List<AulaDocente> findByIdAula(Integer aulaId);
}
