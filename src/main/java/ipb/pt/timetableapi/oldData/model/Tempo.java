package ipb.pt.timetableapi.oldData.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tempo", schema = "2022_2023_1_horarios")
public class Tempo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "inicio")
    private Time inicio;
    @Column(name = "fim")
    private Time fim;
    @Column(name = "descricao")
    private String descricao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tempo that = (Tempo) o;
        return Objects.equals(inicio, that.inicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inicio);
    }
}
