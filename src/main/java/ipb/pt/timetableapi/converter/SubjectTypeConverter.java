package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.newData.model.SubjectType;
import ipb.pt.timetableapi.newData.repository.SubjectTypeRepository;
import ipb.pt.timetableapi.oldData.model.TipoAula;
import ipb.pt.timetableapi.oldData.repository.TipoAulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubjectTypeConverter {
    @Autowired
    private TipoAulaRepository tipoAulaRepository;

    @Autowired
    private SubjectTypeRepository subjectTypeRepository;

    public void convert() {
        List<TipoAula> tipoAulas = tipoAulaRepository.findAll();
        List<SubjectType> subjectTypes = subjectTypeRepository.findAll();

        for (TipoAula tipoAula : tipoAulas) {
            SubjectType subjectType = new SubjectType();
            subjectType.setId((long) tipoAula.getId());
            subjectType.setName(tipoAula.getNome());
            subjectType.setAbbreviation(tipoAula.getAbrev());

            subjectType.setCor(tipoAula.getCor());
            subjectType.setPrioridade(tipoAula.getPrioridade());
            subjectTypeRepository.save(subjectType);
        }

        subjectTypeRepository.deleteAll();
        subjectTypeRepository.saveAll(subjectTypes);
    }
}
