package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.LessonUnitDto;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LessonUnitService {
    @Autowired
    private LessonUnitRepository lessonUnitRepository;

    public List<LessonUnit> findAll() {
        return lessonUnitRepository.findAll();
    }

    public LessonUnit findById(Long id) {
        return lessonUnitRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found"));
    }

    public LessonUnit create(LessonUnitDto lessonUnitDto) {
        LessonUnit lessonUnit = new LessonUnit();
        BeanUtils.copyProperties(lessonUnitDto, lessonUnit);
        return lessonUnitRepository.save(lessonUnit);
    }

    public LessonUnit update(LessonUnitDto lessonUnitDto, Long id) {
        LessonUnit lessonUnit = lessonUnitRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found"));

        BeanUtils.copyProperties(lessonUnitDto, lessonUnit);
        return lessonUnitRepository.save(lessonUnit);
    }

    public void delete(Long id) {
        LessonUnit lessonUnit = lessonUnitRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found"));

        lessonUnitRepository.delete(lessonUnit);
    }
}

