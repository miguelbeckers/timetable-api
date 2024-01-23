package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.LessonDto;
import ipb.pt.timetableapi.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LessonConverter {
    public LessonDto toDto(Lesson lesson) {
        LessonDto lessonDto = new LessonDto();
        BeanUtils.copyProperties(lesson, lessonDto);
        return lessonDto;
    }

    public List<LessonDto> toDto(List<Lesson> lessons) {
        List<LessonDto> lessonDtos = new ArrayList<>();
        for (Lesson lesson : lessons) {
            lessonDtos.add(toDto(lesson));
        }

        return lessonDtos;
    }

    public Lesson toModel(LessonDto lessonDto) {
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(lessonDto, lesson);

        SubjectCourse subjectCourse = new SubjectCourse();
        subjectCourse.setId(lessonDto.getSubjectCourseId());
        lesson.setSubjectCourse(subjectCourse);

        SubjectType subjectType = new SubjectType();
        subjectType.setId(lessonDto.getSubjectTypeId());
        lesson.setSubjectType(subjectType);

        lesson.setProfessors(lessonDto.getProfessorIds().stream()
                .map(professorId -> {
                    Professor professor = new Professor();
                    professor.setId(professorId);
                    return professor;
                })
                .toList());

        lesson.setLessonResources(lessonDto.getLessonResourceIds().stream()
                .map(lessonResourceId -> {
                    LessonResource lessonResource = new LessonResource();
                    lessonResource.setId(lessonResourceId);
                    return lessonResource;
                })
                .toList());

        lesson.setStudents(lessonDto.getStudentIds().stream()
                .map(studentId -> {
                    Student student = new Student();
                    student.setId(studentId);
                    return student;
                })
                .toList());

        return lesson;
    }

    public List<Lesson> toModel(List<LessonDto> lessonDtos) {
        List<Lesson> lessons = new ArrayList<>();
        for (LessonDto lessonDto : lessonDtos) {
            lessons.add(toModel(lessonDto));
        }

        return lessons;
    }
}
