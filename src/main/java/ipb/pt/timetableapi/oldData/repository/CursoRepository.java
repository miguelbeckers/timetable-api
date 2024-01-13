package ipb.pt.timetableapi.oldData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.oldData.model.Curso;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    @Query(value = "SELECT ano_curso.id, detalhes_aula.id_discip, detalhes_aula.id_tipo_aula, docente.abrev, turma, detalhes_aula.id_aula, detalhes_aula.id FROM detalhes_aula " +
            "INNER JOIN ano_curso ON detalhes_aula.id_ano = ano_curso.id_ano AND detalhes_aula.id_curso = ano_curso.id_curso " +
            "INNER JOIN aula_docente ON detalhes_aula.id_aula = aula_docente.id_aula " +
            "INNER JOIN docente ON aula_docente.id_docente = docente.id", nativeQuery = true)
    List<Object[]> findCursoItems();
}
