package ipb.pt.timetableapi.oldData.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.oldData.model.MarcacaoExame;
import ipb.pt.timetableapi.oldData.repository.MarcacaoExameRepository;

import java.util.List;

@Service
public class MarcacaoExameService {
    @Autowired
    private MarcacaoExameRepository marcacaoExameRepository;

    public List<MarcacaoExame> findAll(){
        return marcacaoExameRepository.findAll();
    }

    public MarcacaoExame findById(Integer id){
        return marcacaoExameRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "marcacao-exame não encontrada"));
    }
}
