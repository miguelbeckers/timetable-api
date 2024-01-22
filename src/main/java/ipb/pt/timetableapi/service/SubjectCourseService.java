package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.SubjectCourseDto;
import ipb.pt.timetableapi.model.SubjectCourse;
import ipb.pt.timetableapi.repository.SubjectCourseRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectCourseService {
    @Autowired
    private SubjectCourseRepository subjectCourseRepository;

    public List<SubjectCourse> findAll() {
        return subjectCourseRepository.findAll();
    }

    public SubjectCourse findById(Long id) {
        return subjectCourseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectCourse not found"));
    }

    public SubjectCourse create(SubjectCourseDto subjectCourseDto) {
        SubjectCourse subjectCourse = new SubjectCourse();
        BeanUtils.copyProperties(subjectCourseDto, subjectCourse);
        return subjectCourseRepository.save(subjectCourse);
    }

    public SubjectCourse update(SubjectCourseDto subjectCourseDto, Long id) {
        SubjectCourse subjectCourse = subjectCourseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectCourse not found"));

        BeanUtils.copyProperties(subjectCourseDto, subjectCourse);
        return subjectCourseRepository.save(subjectCourse);
    }

    public void delete(Long id) {
        SubjectCourse subjectCourse = subjectCourseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectCourse not found"));

        subjectCourseRepository.delete(subjectCourse);
    }

    public void deleteAll() {
        subjectCourseRepository.deleteAll();
    }

    public void createMany(List<SubjectCourseDto> subjectCourseDtos) {
        List<SubjectCourse> subjectCourses = new ArrayList<>();

        for (SubjectCourseDto subjectCourseDto : subjectCourseDtos) {
            SubjectCourse subjectCourse = new SubjectCourse();
            BeanUtils.copyProperties(subjectCourseDto, subjectCourse);
            subjectCourses.add(subjectCourse);
        }

        subjectCourseRepository.saveAll(subjectCourses);
    }
}

