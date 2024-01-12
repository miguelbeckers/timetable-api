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
@Table(name = "tipo_aula_disciplina", schema = "2022_2023_1_horarios")
public class TipoAulaDisciplina {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "id_discip")
    private int idDiscip;
    @Column(name = "id_tipo_aula")
    private int idTipoAula;
    @Column(name = "horas_semanais")
    private double horasSemanais;
    @Column(name = "num_blocos")
    private Integer numBlocos;
    @Column(name = "semanal")
    private String semanal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoAulaDisciplina that = (TipoAulaDisciplina) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
