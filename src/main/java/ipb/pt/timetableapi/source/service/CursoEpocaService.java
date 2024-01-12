package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.CursoEpoca;
import ipb.pt.timetableapi.source.repository.CursoEpocaRepository;

import java.util.List;

@Service
public class CursoEpocaService {
    @Autowired
    private CursoEpocaRepository cursoEpocaRepository;

    public List<CursoEpoca> findAll(){
        return cursoEpocaRepository.findAll();
    }

    public CursoEpoca findById(Integer id){
        return cursoEpocaRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "curso-epoca não encontrado"));
    }
}
