package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.LessonUnitConverter;
import ipb.pt.timetableapi.dto.LessonUnitDto;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.repository.LessonRepository;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import ipb.pt.timetableapi.solver.TimetableConstraintConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalTime;
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

//    public List<LessonUnit> splitBlocks() {
//        List<Lesson> lessons = lessonRepository.findAll();
//
//        List<LessonUnit> lessonUnits = new ArrayList<>();
//        long id = 1L;
//
//        for (Lesson lesson : lessons) {
//            double blockSize = Math.round(lesson.getHoursPerWeek() / lesson.getBlocks() * 100) / 100.0;
//
//            for (int i = 0; i < lesson.getBlocks(); i++) {
//                LessonUnit lessonUnit = new LessonUnit();
//                lessonUnit.setId(id++);
//                lessonUnit.setLesson(lesson);
//                lessonUnit.setBlockSize(blockSize);
//                lessonUnits.add(lessonUnit);
//            }
//        }
//
//        return lessonUnits;
//    }

//    public static List<LessonUnit> splitBlocks(List<LessonUnit> lessonUnits, double blockSize) {
//        List<LessonUnit> splitLessonUnits = new ArrayList<>();
//        long id = 1L;
//
//        for (LessonUnit lessonUnit : lessonUnits) {
//            double remainingSize = lessonUnit.getBlockSize();
//            Timeslot timeslot = lessonUnit.getTimeslot();
//
//            for (int i = 0; i < blockSize; i++) {
//                remainingSize -= blockSize;
//
//                LocalTime startTime = timeslot.getStartTime();
//                LocalTime endTime = startTime.plus(Duration.ofMinutes(
//                        (long) blockSize * TimetableConstraintConstants.UNIT));
//
//                Timeslot blockTimeslot = new Timeslot();
//                blockTimeslot.setStartTime(startTime);
//                blockTimeslot.setEndTime(endTime);
//
//                LessonUnit splitLessonUnit = new LessonUnit();
//                splitLessonUnit.setId(id++);
//                splitLessonUnit.setLesson(lessonUnit.getLesson());
//                splitLessonUnit.setBlockSize(Math.min(remainingSize, blockSize));
//                splitLessonUnit.setTimeslot(blockTimeslot);
//                splitLessonUnit.setClassroom(lessonUnit.getClassroom());
//                splitLessonUnits.add(splitLessonUnit);
//
//                timeslot.setStartTime(endTime);
//            }
//        }
//
//        return splitLessonUnits;
//    }

    public List<LessonUnit> getLessonUnitsAsBlocks() {
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();
        return getLessonUnitsAsBlocks(lessonUnits);
    }

    public List<LessonUnit> getLessonUnitsAsBlocks(List<LessonUnit> lessonUnits){
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
            double unitSize = (double) TimetableConstraintConstants.UNIT / TimetableConstraintConstants.HOUR;
            int unitsPerBlock = (int) (blockSize / unitSize);

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

