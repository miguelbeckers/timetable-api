package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.Evento;
import ipb.pt.timetableapi.source.repository.EventoRepository;

import java.util.List;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> findAll(){
        return eventoRepository.findAll();
    }

    public Evento findById(Integer id){
        return eventoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "evento não encontrado"));
    }
}
