package ipb.pt.timetableapi.service;


import ipb.pt.timetableapi.dto.CourseDto;
import ipb.pt.timetableapi.model.Course;
import ipb.pt.timetableapi.repository.CourseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
    }

    public Course create(CourseDto courseDto) {
        Course course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        return courseRepository.save(course);
    }

    public Course update(CourseDto courseDto, Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        BeanUtils.copyProperties(courseDto, course);
        return courseRepository.save(course);
    }

    public void delete(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        courseRepository.delete(course);
    }

    public void deleteAll() {
        courseRepository.deleteAll();
    }

    public void createMany(List<CourseDto> courseDtos) {
        List<Course> courses = new ArrayList<>();

        for (CourseDto courseDto : courseDtos) {
            Course course = new Course();
            BeanUtils.copyProperties(courseDto, course);
            courses.add(course);
        }
        courseRepository.saveAll(courses);
    }
}

