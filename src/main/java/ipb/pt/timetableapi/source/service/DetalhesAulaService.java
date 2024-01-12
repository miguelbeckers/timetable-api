package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.DetalhesAula;
import ipb.pt.timetableapi.source.repository.DetalhesAulaRepository;

import java.util.List;

@Service
public class DetalhesAulaService {
    @Autowired
    private DetalhesAulaRepository detalhesAulaRepository;

    public List<DetalhesAula> findAll(){
        return detalhesAulaRepository.findAll();
    }

    public DetalhesAula findById(Integer id){
        return detalhesAulaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "detalhes-aula não encontrado"));
    }
}
