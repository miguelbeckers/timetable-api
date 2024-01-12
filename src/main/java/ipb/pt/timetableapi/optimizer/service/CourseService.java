package ipb.pt.timetableapi.optimizer.service;


import ipb.pt.timetableapi.optimizer.model.Course;
import ipb.pt.timetableapi.optimizer.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

    public Optional<Course> findById(Long id){
        return courseRepository.findById(id);
    }

    public Course create(Course course){
        return courseRepository.save(course);
    }

    public Course update(Course course){
        return courseRepository.save(course);
    }

    public void delete(Course course){
        courseRepository.delete(course);
    }
}
