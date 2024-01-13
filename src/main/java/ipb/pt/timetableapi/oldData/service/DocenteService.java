package ipb.pt.timetableapi.oldData.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.oldData.model.Docente;
import ipb.pt.timetableapi.oldData.repository.DocenteRepository;

import java.util.List;

@Service
public class DocenteService {
    @Autowired
    private DocenteRepository docenteRepository;

    public List<Docente> findAll(){
        return docenteRepository.findAll();
    }

    public Docente findById(Integer id){
        return docenteRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "docente não encontrado"));
    }
}
