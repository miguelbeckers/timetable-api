package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.CourseDto;
import ipb.pt.timetableapi.model.Course;
import ipb.pt.timetableapi.model.Department;
import ipb.pt.timetableapi.model.Period;
import ipb.pt.timetableapi.model.Timeslot;
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

        if (courseDto.getDepartmentId() != null) {
            Department department = new Department();
            department.setId(courseDto.getDepartmentId());
            course.setDepartment(department);
        }

        course.setUnavailability(courseDto.getUnavailabilityIds().stream()
                .map(id -> {
                    Timeslot unavailability = new Timeslot();
                    unavailability.setId(id);
                    return unavailability;
                })
                .toList());

        course.setPeriods(courseDto.getPeriodIds().stream()
                .map(id -> {
                    Period coursePeriod = new Period();
                    coursePeriod.setId(id);
                    return coursePeriod;
                })
                .toList());

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
