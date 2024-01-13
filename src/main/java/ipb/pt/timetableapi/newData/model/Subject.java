package ipb.pt.timetableapi.newData.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Subject {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String code;

    private Integer idDepart;
    private Integer ipbCodEscola;
    private Integer ipbCodCurso;
    private Integer ipbNPlano;
    private Integer ipbNDisciplina;
    private Integer ipbNOpcao;
}
