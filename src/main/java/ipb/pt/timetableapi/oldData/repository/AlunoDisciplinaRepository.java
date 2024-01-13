package ipb.pt.timetableapi.oldData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.oldData.model.AlunoDisciplina;

import java.util.Optional;

@Repository
public interface AlunoDisciplinaRepository extends JpaRepository<AlunoDisciplina, Integer> {
    Optional<AlunoDisciplina> findById(int id);

    @Query(value = "SELECT * FROM aluno_disciplina WHERE id_aluno = :idAluno", nativeQuery = true)
    Optional<AlunoDisciplina> findByAlunoId(@Param("idAluno") int idAluno);
}
