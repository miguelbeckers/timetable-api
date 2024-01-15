package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.Professor;
import ipb.pt.timetableapi.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    public Professor findById(Long id) {
        return professorRepository.findById(id).orElse(null);
    }

    public Professor create(Professor professor) {
        return professorRepository.save(professor);
    }

    public Professor update(Professor professor) {
        return professorRepository.save(professor);
    }

    public void delete(Professor professor) {
        professorRepository.delete(professor);
    }
}
