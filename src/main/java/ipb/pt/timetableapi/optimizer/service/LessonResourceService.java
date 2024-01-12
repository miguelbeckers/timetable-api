package ipb.pt.timetableapi.optimizer.service;

import ipb.pt.timetableapi.optimizer.model.LessonResource;
import ipb.pt.timetableapi.optimizer.repository.LessonResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonResourceService {
    @Autowired
    private LessonResourceRepository lessonResourceRepository;

    public List<LessonResource> findAll(){
        return lessonResourceRepository.findAll();
    }

    public Optional<LessonResource> findById(Long id){
        return lessonResourceRepository.findById(id);
    }

    public LessonResource create(LessonResource lessonResource){
        return lessonResourceRepository.save(lessonResource);
    }

    public LessonResource update(LessonResource lessonResource){
        return lessonResourceRepository.save(lessonResource);
    }

    public void delete(LessonResource lessonResource){
        lessonResourceRepository.delete(lessonResource);
    }
}
