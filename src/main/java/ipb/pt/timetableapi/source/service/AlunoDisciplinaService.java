package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.AlunoDisciplina;
import ipb.pt.timetableapi.source.repository.AlunoDisciplinaRepository;

import java.util.List;

@Service
public class AlunoDisciplinaService {
    @Autowired
    private AlunoDisciplinaRepository alunoDisciplinaRepository;

    public List<AlunoDisciplina> findAll(){
        return alunoDisciplinaRepository.findAll();
    }

    public AlunoDisciplina findById(Integer id){
        return alunoDisciplinaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "aluno-diciplina não encontrado"));
    }
}
