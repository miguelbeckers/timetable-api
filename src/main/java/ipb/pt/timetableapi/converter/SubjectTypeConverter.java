package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.SubjectTypeDto;
import ipb.pt.timetableapi.model.SubjectType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubjectTypeConverter {
    public SubjectTypeDto toDto(SubjectType subjectType) {
        SubjectTypeDto subjectTypeDto = new SubjectTypeDto();
        BeanUtils.copyProperties(subjectType, subjectTypeDto);
        return subjectTypeDto;
    }

    public List<SubjectTypeDto> toDto(List<SubjectType> subjectTypes) {
        List<SubjectTypeDto> subjectTypeDtos = new ArrayList<>();
        for (SubjectType subjectType : subjectTypes) {
            subjectTypeDtos.add(toDto(subjectType));
        }

        return subjectTypeDtos;
    }

    public SubjectType toModel(SubjectTypeDto subjectTypeDto) {
        SubjectType subjectType = new SubjectType();
        BeanUtils.copyProperties(subjectTypeDto, subjectType);
        return subjectType;
    }

    public List<SubjectType> toModel(List<SubjectTypeDto> subjectTypeDtos) {
        List<SubjectType> subjectTypes = new ArrayList<>();
        for (SubjectTypeDto subjectTypeDto : subjectTypeDtos) {
            subjectTypes.add(toModel(subjectTypeDto));
        }

        return subjectTypes;
    }
}
