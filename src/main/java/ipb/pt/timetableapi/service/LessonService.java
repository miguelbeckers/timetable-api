package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    public List<Lesson> findAll(){
        return lessonRepository.findAll();
    }

    public Optional<Lesson> findById(Long id){
        return lessonRepository.findById(id);
    }

    public Lesson create(Lesson lesson){
        return lessonRepository.save(lesson);
    }

    public Lesson update(Lesson lesson){
        return lessonRepository.save(lesson);
    }

    public void delete(Lesson lesson){
        lessonRepository.delete(lesson);
    }
}