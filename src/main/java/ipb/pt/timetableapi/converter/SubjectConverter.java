package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.newData.model.Period;
import ipb.pt.timetableapi.newData.model.Subject;
import ipb.pt.timetableapi.newData.repository.SubjectRepository;
import ipb.pt.timetableapi.oldData.model.Ano;
import ipb.pt.timetableapi.oldData.model.Disciplina;
import ipb.pt.timetableapi.oldData.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubjectConverter {
    @Autowired
    DisciplinaRepository disciplinaRepository;

    @Autowired
    SubjectRepository subjectRepository;

    public void convert() {
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        List<Subject> subjects = new ArrayList<>();

        for(Disciplina disciplina : disciplinas) {
            Subject subject = new Subject();
            subject.setId((long) disciplina.getId());
            subject.setName(disciplina.getNome());
            subject.setCode(disciplina.getAbrev());

            subject.setIdDepart(disciplina.getIdDepart());
            subject.setIpbCodEscola(disciplina.getIpbCodEscola());
            subject.setIpbCodCurso(disciplina.getIpbCodCurso());
            subject.setIpbNPlano(disciplina.getIpbNPlano());
            subject.setIpbNDisciplina(disciplina.getIpbNDisciplina());
            subject.setIpbNOpcao(disciplina.getIpbNOpcao());
            subjects.add(subject);
        }

        subjectRepository.deleteAll();
        subjectRepository.saveAll(subjects);
    }
}
