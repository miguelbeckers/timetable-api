package ipb.pt.timetableapi.oldData.model;

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
@Table(name = "recurso_disciplina", schema = "2022_2023_1_horarios")
public class RecursoDisciplina {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "id_rec")
    private int idRec;
    @Column(name = "id_curso")
    private int idCurso;
    @Column(name = "id_ano")
    private int idAno;
    @Column(name = "id_discip")
    private int idDiscip;
    @Column(name = "id_tipo_aula")
    private int idTipoAula;
    @Column(name = "quantidade")
    private double quantidade;
    @Column(name = "valor_exacto")
    private String valorExacto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecursoDisciplina that = (RecursoDisciplina) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
