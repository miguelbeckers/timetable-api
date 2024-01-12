package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.Dia;
import ipb.pt.timetableapi.source.repository.DiaRepository;

import java.util.List;

@Service
public class DiaService {
    @Autowired
    private DiaRepository diaRepository;

    public List<Dia> findAll(){
        return diaRepository.findAll();
    }

    public Dia findById(Integer id){
        return diaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "dia não encontrado"));
    }
}
