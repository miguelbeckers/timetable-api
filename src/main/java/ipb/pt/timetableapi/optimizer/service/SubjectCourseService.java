package ipb.pt.timetableapi.optimizer.service;

import ipb.pt.timetableapi.optimizer.model.SubjectCourse;
import ipb.pt.timetableapi.optimizer.repository.SubjectCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectCourseService {
    @Autowired
    private SubjectCourseRepository subjectCourseRepository;

    public List<SubjectCourse> findAll(){
        return subjectCourseRepository.findAll();
    }

    public Optional<SubjectCourse> findById(Long id){
        return subjectCourseRepository.findById(id);
    }

    public SubjectCourse create(SubjectCourse subjectCourse){
        return subjectCourseRepository.save(subjectCourse);
    }

    public SubjectCourse update(SubjectCourse subjectCourse){
        return subjectCourseRepository.save(subjectCourse);
    }

    public void delete(SubjectCourse subjectCourse){
        subjectCourseRepository.delete(subjectCourse);
    }
}
