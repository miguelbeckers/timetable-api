package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.ProfessorDto;
import ipb.pt.timetableapi.model.Professor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfessorConverter {
    public ProfessorDto toDto(Professor professor) {
        ProfessorDto professorDto = new ProfessorDto();
        BeanUtils.copyProperties(professor, professorDto);
        return professorDto;
    }

    public List<ProfessorDto> toDto(List<Professor> professors) {
        List<ProfessorDto> professorDtos = new ArrayList<>();
        for (Professor professor : professors) {
            professorDtos.add(toDto(professor));
        }

        return professorDtos;
    }

    public Professor toModel(ProfessorDto professorDto) {
        Professor professor = new Professor();
        BeanUtils.copyProperties(professorDto, professor);
        return professor;
    }

    public List<Professor> toModel(List<ProfessorDto> professorDtos) {
        List<Professor> professors = new ArrayList<>();
        for (ProfessorDto professorDto : professorDtos) {
            professors.add(toModel(professorDto));
        }

        return professors;
    }
}
