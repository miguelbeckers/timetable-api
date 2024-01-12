package ipb.pt.timetableapi.source.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ipb.pt.timetableapi.source.model.Departamento;
import ipb.pt.timetableapi.source.repository.DepartamentoRepository;

import java.util.List;

@Service
public class DepartamentoService {
    @Autowired
    private DepartamentoRepository departamentoRepository;

    public List<Departamento> findAll(){
        return departamentoRepository.findAll();
    }

    public Departamento findById(Integer id){
        return departamentoRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "departamento não encontrado"));
    }
}
