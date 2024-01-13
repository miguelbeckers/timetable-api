package ipb.pt.timetableapi.oldData.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//It cannot be an entity because it has no id
public class HorarioSemestre {
    @Column(name = "versao")
    private int versao;
    @Column(name = "data")
    private Date data;
    @Column(name = "inicio")
    private Time inicio;
    @Column(name = "fim")
    private Time fim;
    @Column(name = "docentes")
    private String docentes;
    @Column(name = "cursos")
    private String cursos;
    @Column(name = "disciplinas")
    private String disciplinas;
    @Column(name = "sala")
    private String sala;
}
