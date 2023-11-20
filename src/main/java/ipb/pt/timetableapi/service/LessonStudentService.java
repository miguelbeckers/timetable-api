package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.LessonStudent;
import ipb.pt.timetableapi.repository.LessonStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonStudentService {
    @Autowired
    private LessonStudentRepository lessonStudentRepository;

    public List<LessonStudent> findAll(){
        return lessonStudentRepository.findAll();
    }

    public Optional<LessonStudent> findById(Long id){
        return lessonStudentRepository.findById(id);
    }

    public LessonStudent create(LessonStudent lessonStudent){
        return lessonStudentRepository.save(lessonStudent);
    }

    public LessonStudent update(LessonStudent lessonStudent){
        return lessonStudentRepository.save(lessonStudent);
    }

    public void delete(LessonStudent lessonStudent){
        lessonStudentRepository.delete(lessonStudent);
    }
}
