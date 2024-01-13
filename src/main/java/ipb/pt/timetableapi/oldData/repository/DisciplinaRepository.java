package ipb.pt.timetableapi.oldData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.oldData.model.Disciplina;

import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {
    List<Disciplina> findByIdDepart(int idDepart);

}
