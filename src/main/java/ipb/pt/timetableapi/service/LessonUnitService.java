package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.LessonUnitConverter;
import ipb.pt.timetableapi.dto.LessonUnitDto;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.TimeConstant;
import ipb.pt.timetableapi.repository.LessonRepository;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LessonUnitService {
    private final LessonUnitRepository lessonUnitRepository;
    private final LessonUnitConverter lessonUnitConverter;
    private final LessonRepository lessonRepository;

    @Autowired
    public LessonUnitService(LessonUnitRepository lessonUnitRepository,
                             LessonUnitConverter lessonUnitConverter,
                             LessonRepository lessonRepository) {
        this.lessonUnitRepository = lessonUnitRepository;
        this.lessonUnitConverter = lessonUnitConverter;
        this.lessonRepository = lessonRepository;
    }

    public List<LessonUnitDto> findAll() {
        return lessonUnitConverter.toDto(lessonUnitRepository.findAll());
    }

    public LessonUnitDto findById(Long id) {
        return lessonUnitConverter.toDto(lessonUnitRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found")));
    }

    public LessonUnitDto create(LessonUnitDto lessonUnitDto) {
        LessonUnit lessonUnit = lessonUnitConverter.toModel(lessonUnitDto);
        return lessonUnitConverter.toDto(lessonUnitRepository.save(lessonUnit));
    }

    public LessonUnitDto update(LessonUnitDto lessonUnitDto, Long id) {
        lessonUnitRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found"));

        LessonUnit lessonUnit = lessonUnitConverter.toModel(lessonUnitDto);
        return lessonUnitConverter.toDto(lessonUnitRepository.save(lessonUnit));
    }

    public LessonUnit update(LessonUnit lessonUnit) {
        lessonUnitRepository.findById(lessonUnit.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found"));

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

    public List<LessonUnitDto> saveAll(List<LessonUnitDto> lessonUnitDtos) {
        List<LessonUnit> lessonUnits = lessonUnitConverter.toModel(lessonUnitDtos);

        return lessonUnitConverter.toDto(lessonUnitRepository.saveAll(lessonUnits));
    }

    public List<LessonUnitDto> resetAll() {
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();

        lessonUnits.forEach(lessonUnit -> {
            lessonUnit.setTimeslot(null);
            lessonUnit.setClassroom(null);
        });

        return lessonUnitConverter.toDto(lessonUnitRepository.saveAll(lessonUnits));
    }

    public List<LessonUnit> splitInTwoBlocks(List<LessonUnit> lessonUnits, double blockSize) {
        List<LessonUnit> splitLessonUnits = new ArrayList<>();

        for (LessonUnit lessonUnit : lessonUnits) {
            Lesson lesson = lessonUnit.getLesson();
            double remaining = lessonUnit.getBlockSize() - blockSize;

            if(remaining > blockSize){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Block size is too big to be split into blocks of size " + blockSize);
            }

            LessonUnit firstBlock = new LessonUnit();
            firstBlock.setId(lessonUnit.getId());
            firstBlock.setLesson(lesson);
            firstBlock.setBlockSize(blockSize);
            splitLessonUnits.add(firstBlock);

            LessonUnit secondBlock = new LessonUnit();
            secondBlock.setId((long) (lessonUnit.getId() + blockSize / TimeConstant.SLOT));
            secondBlock.setLesson(lesson);
            secondBlock.setBlockSize(remaining);
            splitLessonUnits.add(secondBlock);
        }

        return splitLessonUnits;
    }

    public List<LessonUnit> getLessonUnitsAsBlocks() {
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();
        return getLessonUnitsAsBlocks(lessonUnits);
    }

    public List<LessonUnit> getLessonUnitsAsBlocks(List<LessonUnit> lessonUnits) {
        List<LessonUnit> newLessonUnits = new ArrayList<>();

        while (!lessonUnits.isEmpty()) {
            LessonUnit lessonUnit = lessonUnits.get(0);
            lessonUnits.remove(0);

            List<LessonUnit> lessonUnitsWithSameLesson = new ArrayList<>();
            lessonUnitsWithSameLesson.add(lessonUnit);

            for (int i = 0; i < lessonUnits.size(); i++) {
                if (Objects.equals(lessonUnits.get(i).getLesson().getId(), lessonUnit.getLesson().getId())) {
                    lessonUnitsWithSameLesson.add(lessonUnits.get(i));
                    lessonUnits.remove(i);
                    i--;
                }
            }

            Lesson lesson = lessonUnit.getLesson();
            double blockSize = lesson.getHoursPerWeek() / lesson.getBlocks();
            int unitsPerBlock = (int) (blockSize / TimeConstant.SLOT);

            for (int i = 0; i < lesson.getBlocks(); i++) {
                LessonUnit newLessonUnit = new LessonUnit();
                newLessonUnit.setId(lessonUnitsWithSameLesson.get(0).getId());
                newLessonUnit.setLesson(lesson);
                newLessonUnit.setBlockSize(blockSize);
                newLessonUnits.add(newLessonUnit);

                if (unitsPerBlock > 0) {
                    lessonUnitsWithSameLesson.subList(0, unitsPerBlock).clear();
                }
            }
        }

        return newLessonUnits;
    }
}

