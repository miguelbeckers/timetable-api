package ipb.pt.timetableapi.oldData.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.oldData.model.DisciplinaExame;
import ipb.pt.timetableapi.oldData.repository.DisciplinaExameRepository;

import java.util.List;

@Service
public class DisciplinaExameService {
    @Autowired
    private DisciplinaExameRepository disciplinaExameRepository;

    public List<DisciplinaExame> findAll(){
        return disciplinaExameRepository.findAll();
    }

    public DisciplinaExame findById(Integer id){
        return disciplinaExameRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "disciplina-exame não encontrada"));
    }
}
