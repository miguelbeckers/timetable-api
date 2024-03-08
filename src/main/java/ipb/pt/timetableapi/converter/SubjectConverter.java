package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.SubjectDto;
import ipb.pt.timetableapi.model.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubjectConverter {
    public SubjectDto toDto(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
        BeanUtils.copyProperties(subject, subjectDto);
        return subjectDto;
    }

    public List<SubjectDto> toDto(List<Subject> subjects) {
        List<SubjectDto> subjectDtos = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectDtos.add(toDto(subject));
        }

        return subjectDtos;
    }

    public Subject toModel(SubjectDto subjectDto) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectDto, subject);
        return subject;
    }

    public List<Subject> toModel(List<SubjectDto> subjectDtos) {
        List<Subject> subjects = new ArrayList<>();
        for (SubjectDto subjectDto : subjectDtos) {
            subjects.add(toModel(subjectDto));
        }

        return subjects;
    }
}
