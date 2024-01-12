package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.TipoAula;
import ipb.pt.timetableapi.source.repository.TipoAulaRepository;

import java.util.List;

@Service
public class TipoAulaService {
    @Autowired
    private TipoAulaRepository tipoAulaRepository;

    public List<TipoAula> findAll(){
        return tipoAulaRepository.findAll();
    }

    public TipoAula findById(Integer id){
        return tipoAulaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tipo-aula não encontrado"));
    }
}
