package ipb.pt.timetableapi.oldData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.oldData.model.AnoCurso;

import java.util.List;

@Repository
public interface AnoCursoRepository extends JpaRepository<AnoCurso, Integer> {
    List<AnoCurso> findByIdCurso(int idCurso);
}
