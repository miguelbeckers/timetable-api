package ipb.pt.timetableapi.oldData.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.oldData.model.IndispSalaExame;
import ipb.pt.timetableapi.oldData.repository.IndispSalaExameRepository;

import java.util.List;

@Service
public class IndispSalaExameService {
    @Autowired
    private IndispSalaExameRepository indispSalaExameRepository;

    public List<IndispSalaExame> findAll(){
        return indispSalaExameRepository.findAll();
    }

    public IndispSalaExame findById(Integer id){
        return indispSalaExameRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "indisp-sala-exame não encontrada"));
    }
}
