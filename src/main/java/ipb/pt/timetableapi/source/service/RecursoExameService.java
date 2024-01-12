package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.RecursoExame;
import ipb.pt.timetableapi.source.repository.RecursoExameRepository;

import java.util.List;

@Service
public class RecursoExameService {
    @Autowired
    private RecursoExameRepository recursoExameRepository;

    public List<RecursoExame> findAll(){
        return recursoExameRepository.findAll();
    }

    public RecursoExame findById(Integer id){
        return recursoExameRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recurso-exame não encontrado"));
    }
}
