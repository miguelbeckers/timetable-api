package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonUnitService {
    @Autowired
    private LessonUnitRepository lessonUnitRepository;

    public List<LessonUnit> findAll(){
        return lessonUnitRepository.findAll();
    }

    public Optional<LessonUnit> findById(Long id){
        return lessonUnitRepository.findById(id);
    }

    public LessonUnit create(LessonUnit lessonUnit){
        return lessonUnitRepository.save(lessonUnit);
    }

    public LessonUnit update(LessonUnit lessonUnit){
        return lessonUnitRepository.save(lessonUnit);
    }

    public void delete(LessonUnit lessonUnit){
        lessonUnitRepository.delete(lessonUnit);
    }
}
