package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.LessonUnitConverter;
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
    private final LessonUnitRepository lessonUnitRepository;
    private final LessonUnitConverter lessonUnitConverter;

    @Autowired
    public LessonUnitService(LessonUnitRepository lessonUnitRepository, LessonUnitConverter lessonUnitConverter) {
        this.lessonUnitRepository = lessonUnitRepository;
        this.lessonUnitConverter = lessonUnitConverter;
    }

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

    public void deleteAll() {
        lessonUnitRepository.deleteAll();
    }

    public void saveAll(List<LessonUnitDto> lessonUnitDtos) {
        List<LessonUnit> lessonUnits = lessonUnitConverter.toModel(lessonUnitDtos);
        lessonUnitRepository.saveAll(lessonUnits);
    }

    public List<LessonUnit> resetAll(){
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();

        lessonUnits.forEach(lessonUnit -> {
            lessonUnit.setTimeslot(null);
            lessonUnit.setClassroom(null);
        });

        return lessonUnitRepository.saveAll(lessonUnits);
    }
}

