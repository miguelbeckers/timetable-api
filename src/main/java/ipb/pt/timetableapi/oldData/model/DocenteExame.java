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
@Table(name = "docente_exame", schema = "2022_2023_1_horarios")
public class DocenteExame {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "original")
    private String original;
    @Column(name = "id_docente")
    private int idDocente;
    @Column(name = "id_marcacao")
    private int idMarcacao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocenteExame that = (DocenteExame) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
