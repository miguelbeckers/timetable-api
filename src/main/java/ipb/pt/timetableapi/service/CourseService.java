package ipb.pt.timetableapi.service;


import ipb.pt.timetableapi.converter.CourseConverter;
import ipb.pt.timetableapi.dto.CourseDto;
import ipb.pt.timetableapi.model.Course;
import ipb.pt.timetableapi.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;

    @Autowired
    public CourseService(CourseRepository courseRepository, CourseConverter courseConverter) {
        this.courseRepository = courseRepository;
        this.courseConverter = courseConverter;
    }

    public List<CourseDto> findAll() {
        return courseConverter.toDto(courseRepository.findAll());
    }

    public CourseDto findById(Long id) {
        return courseConverter.toDto(courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found")));
    }

    public CourseDto create(CourseDto courseDto) {
        Course course = courseConverter.toModel(courseDto);
        return courseConverter.toDto(courseRepository.save(course));
    }

    public CourseDto update(CourseDto courseDto, Long id) {
        courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        Course course = courseConverter.toModel(courseDto);
        return courseConverter.toDto(courseRepository.save(course));
    }

    public void delete(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        courseRepository.delete(course);
    }

    public void deleteAll() {
        courseRepository.deleteAll();
    }

    public List<CourseDto> saveAll(List<CourseDto> courseDtos) {
        List<Course> courses = courseConverter.toModel(courseDtos);
        return courseConverter.toDto(courseRepository.saveAll(courses));
    }
}

