package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.AnoCurso;
import ipb.pt.timetableapi.source.repository.AnoCursoRepository;

import java.util.List;

@Service
public class AnoCursoService {
    @Autowired
    private AnoCursoRepository anoCursoRepository;

    public List<AnoCurso> findAll(){
        return anoCursoRepository.findAll();
    }

    public AnoCurso findById(Integer id){
        return anoCursoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ano-curso não encontrado"));
    }
}
