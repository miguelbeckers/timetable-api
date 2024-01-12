package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.EpocaExame;
import ipb.pt.timetableapi.source.repository.EpocaExameRepository;

import java.util.List;

@Service
public class EpocaExameService {
    @Autowired
    private EpocaExameRepository epocaExameRepository;

    public List<EpocaExame> findAll(){
        return epocaExameRepository.findAll();
    }

    public EpocaExame findById(Integer id){
        return epocaExameRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "epoca-exame não encontrada"));
    }
}
