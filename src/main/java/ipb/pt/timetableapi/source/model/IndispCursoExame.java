package ipb.pt.timetableapi.source.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "indisp_curso_exame", schema = "2022_2023_1_horarios")
public class IndispCursoExame {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "id_ano")
    private int idAno;
    @Column(name = "id_curso")
    private int idCurso;
    @Column(name = "dia")
    private Date dia;
    @Column(name = "inicio")
    private Time inicio;
    @Column(name = "fim")
    private Time fim;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndispCursoExame that = (IndispCursoExame) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
