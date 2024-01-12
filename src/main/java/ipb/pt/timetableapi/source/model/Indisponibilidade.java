package ipb.pt.timetableapi.source.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "indisponibilidade", schema = "2022_2023_1_horarios")
public class Indisponibilidade {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "id_tipo")
    private Integer idTipo;
    @Column(name = "id_ano")
    private Integer idAno;
    @Column(name = "id_dia")
    private int idDia;
    @Column(name = "inicio")
    private Time inicio;
    @Column(name = "fim")
    private Time fim;
}
