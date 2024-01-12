package ipb.pt.timetableapi.source.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "disciplina_exame", schema = "2022_2023_1_horarios")
public class DisciplinaExame {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "id_exame")
    private int idExame;
    @Column(name = "id_discip")
    private int idDiscip;
    @Column(name = "id_tipo_aula")
    private int idTipoAula;
    @Column(name = "id_ano")
    private int idAno;
    @Column(name = "id_curso")
    private int idCurso;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisciplinaExame that = (DisciplinaExame) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
