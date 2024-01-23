package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.ProfessorConverter;
import ipb.pt.timetableapi.dto.ProfessorDto;
import ipb.pt.timetableapi.model.Professor;
import ipb.pt.timetableapi.repository.ProfessorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final ProfessorConverter professorConverter;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository, ProfessorConverter professorConverter) {
        this.professorRepository = professorRepository;
        this.professorConverter = professorConverter;
    }

    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Professor findById(Long id) {
        return professorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));
    }

    public Professor create(ProfessorDto professorDto) {
        Professor professor = new Professor();
        BeanUtils.copyProperties(professorDto, professor);
        return professorRepository.save(professor);
    }

    public Professor update(ProfessorDto professorDto, Long id) {
        Professor professor = professorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));

        BeanUtils.copyProperties(professorDto, professor);
        return professorRepository.save(professor);
    }

    public void delete(Long id) {
        Professor professor = professorRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));

        professorRepository.delete(professor);
    }

    public void deleteAll() {
        professorRepository.deleteAll();
    }

    public void createMany(List<ProfessorDto> professorDtos) {
        List<Professor> professors = professorConverter.toModel(professorDtos);
        professorRepository.saveAll(professors);
    }
}

