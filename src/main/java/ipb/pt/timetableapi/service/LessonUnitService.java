package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.constant.TimeslotConstant;
import ipb.pt.timetableapi.converter.LessonUnitConverter;
import ipb.pt.timetableapi.dto.LessonUnitDto;
import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class LessonUnitService {
    private final LessonUnitRepository lessonUnitRepository;
    private final LessonUnitConverter lessonUnitConverter;
    private final TimeslotRepository timeslotRepository;

    @Autowired
    public LessonUnitService(LessonUnitRepository lessonUnitRepository,
                             LessonUnitConverter lessonUnitConverter,
                             TimeslotRepository timeslotRepository) {
        this.lessonUnitRepository = lessonUnitRepository;
        this.lessonUnitConverter = lessonUnitConverter;
        this.timeslotRepository = timeslotRepository;
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

    public List<LessonUnit> divideLessonBlocks(List<LessonUnit> lessonBlocks) {
        List<LessonUnit> dividedLessonUnits = new ArrayList<>();
        List<Timeslot> timeslots = timeslotRepository.findAll();

        HashMap<Long, Timeslot> timeslotMap = new HashMap<>();
        timeslots.forEach(timeslot -> timeslotMap.put(timeslot.getId(), timeslot));

        for (LessonUnit lessonBlock : lessonBlocks) {
            double blockSize = lessonBlock.getBlockSize();
            Long id = lessonBlock.getId();
            Lesson lesson = lessonBlock.getLesson();
            Classroom classroom = lessonBlock.getClassroom();
            Timeslot timeslot = lessonBlock.getTimeslot();
            int units = (int) (blockSize / TimeslotConstant.SIZE_0_5);

            for (int i = 0; i < units; i++) {
                LessonUnit lessonUnit = new LessonUnit();
                lessonUnit.setId(id + i);
                lessonUnit.setBlockSize(TimeslotConstant.SIZE_0_5);
                lessonUnit.setLesson(lesson);
                lessonUnit.setClassroom(classroom);
                lessonUnit.setTimeslot(timeslot == null ? null : timeslotMap.get(timeslot.getId() + i));
                dividedLessonUnits.add(lessonUnit);
            }
        }

        return dividedLessonUnits;
    }

    public List<LessonUnit> getLessonBlocks(double blockSize) {
        List<LessonUnit> lessonUnitsAsBlocks = getLessonUnitsAsBlocks();

        if (blockSize == TimeslotConstant.SIZE_5) {
            return lessonUnitsAsBlocks.stream()
                    .filter(lessonUnit -> lessonUnit.getBlockSize() <= TimeslotConstant.SIZE_5
                            && lessonUnit.getBlockSize() > TimeslotConstant.SIZE_2_5).toList();
        }

        if (blockSize == TimeslotConstant.SIZE_2_5) {
            return lessonUnitsAsBlocks.stream()
                    .filter(lessonUnit -> lessonUnit.getBlockSize() <= TimeslotConstant.SIZE_2_5
                            && lessonUnit.getBlockSize() > TimeslotConstant.SIZE_1).toList();
        }

        if (blockSize == TimeslotConstant.SIZE_1) {
            return lessonUnitsAsBlocks.stream()
                    .filter(lessonUnit -> lessonUnit.getBlockSize() <= TimeslotConstant.SIZE_1
                            && lessonUnit.getBlockSize() > TimeslotConstant.SIZE_0_5).toList();
        }

        if (blockSize == TimeslotConstant.SIZE_0_5) {
            return lessonUnitsAsBlocks.stream()
                    .filter(lessonUnit -> lessonUnit.getBlockSize() == TimeslotConstant.SIZE_0_5).toList();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Block size " + blockSize + " is not valid");
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
            Lesson lesson = lessonUnit.getLesson();

            for (int i = 0; i < lessonUnits.size(); i++) {
                if (Objects.equals(lessonUnits.get(i).getLesson().getId(), lesson.getId())) {
                    lessonUnitsWithSameLesson.add(lessonUnits.get(i));
                    lessonUnits.remove(i);
                    i--;
                }
            }

            double blockSize = lesson.getHoursPerWeek() / lesson.getBlocks();
            int unitsPerBlock = (int) (blockSize / TimeslotConstant.SIZE_0_5);

            for (int i = 0; i < lesson.getBlocks(); i++) {
                LessonUnit newLessonUnit = new LessonUnit();
                newLessonUnit.setId(lessonUnitsWithSameLesson.get(0).getId());
                newLessonUnit.setTimeslot(lessonUnitsWithSameLesson.get(0).getTimeslot());
                // TODO: test if it is necessary to update the restrictions
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

