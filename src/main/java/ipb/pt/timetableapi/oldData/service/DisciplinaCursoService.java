package ipb.pt.timetableapi.oldData.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.oldData.model.DisciplinaCurso;
import ipb.pt.timetableapi.oldData.repository.DisciplinaCursoRepository;

import java.util.List;

@Service
public class DisciplinaCursoService {
    @Autowired
    private DisciplinaCursoRepository disciplinaCursoRepository;

    public List<DisciplinaCurso> findAll(){
        return disciplinaCursoRepository.findAll();
    }

    public DisciplinaCurso findById(Integer id){
        return disciplinaCursoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "disciplina-curso não encontrada"));
    }
}
