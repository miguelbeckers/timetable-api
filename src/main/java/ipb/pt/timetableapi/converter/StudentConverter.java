package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.StudentDto;
import ipb.pt.timetableapi.model.Student;
import ipb.pt.timetableapi.model.SubjectCourse;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudentConverter {
    public StudentDto toDto(Student student) {
        StudentDto studentDto = new StudentDto();
        BeanUtils.copyProperties(student, studentDto);
        return studentDto;
    }

    public List<StudentDto> toDto(List<Student> students) {
        List<StudentDto> studentDtos = new ArrayList<>();
        for (Student student : students) {
            studentDtos.add(toDto(student));
        }

        return studentDtos;
    }

    public Student toModel(StudentDto studentDto) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDto, student);

        student.setSubjectCourses(studentDto.getSubjectCourseIds().stream()
                .map(subjectCourseId -> {
                    SubjectCourse subjectCourse = new SubjectCourse();
                    subjectCourse.setId(subjectCourseId);
                    return subjectCourse;
                })
                .toList());

        return student;
    }

    public List<Student> toModel(List<StudentDto> studentDtos) {
        List<Student> students = new ArrayList<>();
        for (StudentDto studentDto : studentDtos) {
            students.add(toModel(studentDto));
        }

        return students;
    }
}
