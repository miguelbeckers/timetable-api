package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.SubjectCourseDto;
import ipb.pt.timetableapi.model.SubjectCourse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SubjectCourseConverter {
    public SubjectCourseDto toDto(SubjectCourse subjectCourse) {
        SubjectCourseDto subjectCourseDto = new SubjectCourseDto();
        BeanUtils.copyProperties(subjectCourse, subjectCourseDto);
        return subjectCourseDto;
    }

    public List<SubjectCourseDto> toDto(List<SubjectCourse> subjectCourses) {
        List<SubjectCourseDto> subjectCourseDtos = new ArrayList<>();
        for (SubjectCourse subjectCourse : subjectCourses) {
            subjectCourseDtos.add(toDto(subjectCourse));
        }

        return subjectCourseDtos;
    }

    public SubjectCourse toModel(SubjectCourseDto subjectCourseDto) {
        SubjectCourse subjectCourse = new SubjectCourse();
        BeanUtils.copyProperties(subjectCourseDto, subjectCourse);
        return subjectCourse;
    }

    public List<SubjectCourse> toModel(List<SubjectCourseDto> subjectCourseDtos) {
        List<SubjectCourse> subjectCourses = new ArrayList<>();
        for (SubjectCourseDto subjectCourseDto : subjectCourseDtos) {
            subjectCourses.add(toModel(subjectCourseDto));
        }

        return subjectCourses;
    }
}
