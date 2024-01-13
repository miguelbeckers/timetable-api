package ipb.pt.timetableapi.oldData.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "aluno_disciplina", schema = "2022_2023_1_horarios")
public class AlunoDisciplina {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "id_aluno")
    private int idAluno;
    @Column(name = "id_discip")
    private int idDiscip;
    @Column(name = "id_curso")
    private int idCurso;
    @Column(name = "ano")
    private int ano;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlunoDisciplina that = (AlunoDisciplina) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
