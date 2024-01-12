package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.RecursoSala;
import ipb.pt.timetableapi.source.repository.RecursoSalaRepository;

import java.util.List;

@Service
public class RecursoSalaService {
    @Autowired
    private RecursoSalaRepository recursoSalaRepository;

    public List<RecursoSala> findAll(){
        return recursoSalaRepository.findAll();
    }

    public RecursoSala findById(Integer id){
        return recursoSalaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "recurso-sala não encontrado"));
    }
}
