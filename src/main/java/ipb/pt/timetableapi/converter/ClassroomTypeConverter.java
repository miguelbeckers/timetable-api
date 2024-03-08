package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.ClassroomTypeDto;
import ipb.pt.timetableapi.model.ClassroomType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClassroomTypeConverter {
    public ClassroomTypeDto toDto(ClassroomType classroomType) {
        ClassroomTypeDto classroomTypeDto = new ClassroomTypeDto();
        BeanUtils.copyProperties(classroomType, classroomTypeDto);
        return classroomTypeDto;
    }

    public List<ClassroomTypeDto> toDto(List<ClassroomType> classroomTypes) {
        List<ClassroomTypeDto> classroomTypeDtos = new ArrayList<>();
        for (ClassroomType classroomType : classroomTypes) {
            classroomTypeDtos.add(toDto(classroomType));
        }

        return classroomTypeDtos;
    }

    public ClassroomType toModel(ClassroomTypeDto classroomTypeDto) {
        ClassroomType classroomType = new ClassroomType();
        BeanUtils.copyProperties(classroomTypeDto, classroomType);
        return classroomType;
    }

    public List<ClassroomType> toModel(List<ClassroomTypeDto> classroomTypeDtos) {
        List<ClassroomType> classroomTypes = new ArrayList<>();
        for (ClassroomTypeDto classroomTypeDto : classroomTypeDtos) {
            classroomTypes.add(toModel(classroomTypeDto));
        }

        return classroomTypes;
    }
}
