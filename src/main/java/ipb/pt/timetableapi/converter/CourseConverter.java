package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.CourseDto;
import ipb.pt.timetableapi.model.Course;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseConverter {
    public CourseDto toDto(Course course) {
        CourseDto courseDto = new CourseDto();
        BeanUtils.copyProperties(course, courseDto);
        return courseDto;
    }

    public List<CourseDto> toDto(List<Course> courses) {
        List<CourseDto> courseDtos = new ArrayList<>();
        for (Course course : courses) {
            courseDtos.add(toDto(course));
        }

        return courseDtos;
    }

    public Course toModel(CourseDto courseDto) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        return course;
    }

    public List<Course> toModel(List<CourseDto> courseDtos) {
        List<Course> courses = new ArrayList<>();
        for (CourseDto courseDto : courseDtos) {
            courses.add(toModel(courseDto));
        }

        return courses;
    }
}
