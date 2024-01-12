package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.IndispEscolaExame;
import ipb.pt.timetableapi.source.repository.IndispEscolaExameRepository;

import java.util.List;

@Service
public class IndispEscolaExameService {
    @Autowired
    private IndispEscolaExameRepository indispEscolaExameRepository;

    public List<IndispEscolaExame> findAll(){
        return indispEscolaExameRepository.findAll();
    }

    public IndispEscolaExame findById(Integer id){
        return indispEscolaExameRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "indisp-sala-exame não encontrada"));
    }
}
