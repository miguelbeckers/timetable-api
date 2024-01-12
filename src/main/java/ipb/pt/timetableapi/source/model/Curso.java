package ipb.pt.timetableapi.source.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "curso", schema = "2022_2023_1_horarios")
public class Curso {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "nome")
    private String nome;
    @Column(name = "abrev")
    private String abrev;
    @Column(name = "ipb_cod_escola")
    private Integer ipbCodEscola;
    @Column(name = "ipb_cod_curso")
    private Integer ipbCodCurso;
    @Column(name = "ipb_n_plano")
    private Integer ipbNPlano;
    @Column(name = "inicio_aulas")
    private Date inicioAulas;
    @Column(name = "fim_aulas")
    private Date fimAulas;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso that = (Curso) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
