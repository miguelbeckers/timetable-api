package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.TipoSala;
import ipb.pt.timetableapi.source.repository.TipoSalaRepository;

import java.util.List;

@Service
public class TipoSalaService {
    @Autowired
    private TipoSalaRepository tipoSalaRepository;

    public List<TipoSala> findAll(){
        return tipoSalaRepository.findAll();
    }

    public TipoSala findById(Integer id){
        return tipoSalaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tipo-sala não encontrado"));
    }
}
