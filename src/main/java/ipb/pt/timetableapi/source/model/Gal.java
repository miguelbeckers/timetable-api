package ipb.pt.timetableapi.source.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//It cannot be an entity because it has no id
public class Gal {
    @Column(name = "inicio")
    private Date inicio;
    @Column(name = "fim")
    private Date fim;
    @Column(name = "semestre")
    private int semestre;
    @Column(name = "versao")
    private int versao;
    @Column(name = "data")
    private Timestamp data;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "ano1")
    private int ano1;
    @Column(name = "ano2")
    private int ano2;
    @Column(name = "cod_escola")
    private Short codEscola;
    @Column(name = "emails")
    private String emails;
}
