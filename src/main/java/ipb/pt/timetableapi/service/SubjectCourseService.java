package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.SubjectCourseConverter;
import ipb.pt.timetableapi.dto.SubjectCourseDto;
import ipb.pt.timetableapi.model.SubjectCourse;
import ipb.pt.timetableapi.repository.SubjectCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubjectCourseService {
    private final SubjectCourseRepository subjectCourseRepository;
    private final SubjectCourseConverter subjectCourseConverter;

    @Autowired
    public SubjectCourseService(SubjectCourseRepository subjectCourseRepository, SubjectCourseConverter subjectCourseConverter) {
        this.subjectCourseRepository = subjectCourseRepository;
        this.subjectCourseConverter = subjectCourseConverter;
    }

    public List<SubjectCourseDto> findAll() {
        return subjectCourseConverter.toDto(subjectCourseRepository.findAll());
    }

    public SubjectCourseDto findById(Long id) {
        return subjectCourseConverter.toDto(subjectCourseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectCourse not found")));
    }

    public SubjectCourseDto create(SubjectCourseDto subjectCourseDto) {
        SubjectCourse subjectCourse = subjectCourseConverter.toModel(subjectCourseDto);
        return subjectCourseConverter.toDto(subjectCourseRepository.save(subjectCourse));
    }

    public SubjectCourseDto update(SubjectCourseDto subjectCourseDto, Long id) {
        subjectCourseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectCourse not found"));

        SubjectCourse subjectCourse = subjectCourseConverter.toModel(subjectCourseDto);
        return subjectCourseConverter.toDto(subjectCourseRepository.save(subjectCourse));
    }

    public void delete(Long id) {
        SubjectCourse subjectCourse = subjectCourseRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubjectCourse not found"));

        subjectCourseRepository.delete(subjectCourse);
    }

    public void deleteAll() {
        subjectCourseRepository.deleteAll();
    }

    public List<SubjectCourseDto> saveAll(List<SubjectCourseDto> subjectCourseDtos) {
        List<SubjectCourse> subjectCourses = subjectCourseConverter.toModel(subjectCourseDtos);
        return subjectCourseConverter.toDto(subjectCourseRepository.saveAll(subjectCourses));
    }
}

