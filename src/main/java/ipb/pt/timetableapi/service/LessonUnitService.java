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
    private final TimeslotService timeslotService;

    @Autowired
    public LessonUnitService(LessonUnitRepository lessonUnitRepository,
                             LessonUnitConverter lessonUnitConverter,
                             TimeslotRepository timeslotRepository,
                             TimeslotService timeslotService) {
        this.lessonUnitRepository = lessonUnitRepository;
        this.lessonUnitConverter = lessonUnitConverter;
        this.timeslotRepository = timeslotRepository;
        this.timeslotService = timeslotService;
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
            lessonUnit.setIsPinned(false);
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
        if (blockSize == TimeslotConstant.SIZE_5) {
            return getLessonBlocks(TimeslotConstant.SIZE_5, TimeslotConstant.SIZE_2_5);
        }

        if (blockSize == TimeslotConstant.SIZE_2_5) {
            return getLessonBlocks(TimeslotConstant.SIZE_2_5, TimeslotConstant.SIZE_1);
        }

        if (blockSize == TimeslotConstant.SIZE_1) {
            return getLessonBlocks(TimeslotConstant.SIZE_1, TimeslotConstant.SIZE_0_5);
        }

        if (blockSize == TimeslotConstant.SIZE_0_5) {
            return getLessonBlocks(TimeslotConstant.SIZE_0_5, TimeslotConstant.SIZE_0);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Block size " + blockSize + " is not valid");
    }

    public List<LessonUnit> getLessonBlocks(double size, double nextSmallerSize) {
        List<LessonUnit> lessonBlocks = getLessonBlocks();

        List<LessonUnit> lessonBlocksOfTheSize = new ArrayList<>(lessonBlocks.stream()
                .filter(lessonUnit -> lessonUnit.getBlockSize() <= size
                        && lessonUnit.getBlockSize() > nextSmallerSize).toList());

        if (size == TimeslotConstant.SIZE_0_5) {
            return lessonBlocksOfTheSize;
        }

        List<LessonUnit> lessonBlocksWithBiggerSize = new ArrayList<>(lessonBlocks.stream()
                .filter(lessonUnit -> lessonUnit.getBlockSize() <= TimeslotConstant.SIZE_5
                        && lessonUnit.getBlockSize() > size).toList());

        List<LessonUnit> lessonBlocksSplitInto2_5 = splitBlocks(lessonBlocksWithBiggerSize, size);
        lessonBlocksOfTheSize.addAll(lessonBlocksSplitInto2_5);

        return lessonBlocksOfTheSize;
    }

    public List<LessonUnit> getLessonBlocks() {
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();
        return getLessonBlocks(lessonUnits);
    }

    public List<LessonUnit> getLessonBlocks(List<LessonUnit> lessonUnits) {
        List<LessonUnit> newLessonUnits = new ArrayList<>();

        while (!lessonUnits.isEmpty()) {
            LessonUnit lessonUnit = lessonUnits.remove(0);

            List<LessonUnit> lessonUnitsWithSameLesson = new ArrayList<>();
            lessonUnitsWithSameLesson.add(lessonUnit);

            Lesson lesson = lessonUnit.getLesson();
            Classroom classroom = lessonUnit.getClassroom();
            boolean isPinned = lessonUnit.getIsPinned();

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
                LessonUnit firstLessonUnit = lessonUnitsWithSameLesson.get(0);
                LessonUnit newLessonUnit = new LessonUnit();
                newLessonUnit.setId(firstLessonUnit.getId());
                newLessonUnit.setTimeslot(firstLessonUnit.getTimeslot());
                newLessonUnit.setLesson(lesson);
                newLessonUnit.setBlockSize(blockSize);
                newLessonUnit.setClassroom(classroom);
                newLessonUnit.setIsPinned(isPinned);
                newLessonUnits.add(newLessonUnit);

                if (unitsPerBlock > 0) {
                    lessonUnitsWithSameLesson.subList(0, unitsPerBlock).clear();
                }
            }
        }

        return newLessonUnits;
    }

    public List<LessonUnit> splitBlocks(List<LessonUnit> lessonBlocks, double blockSize) {
        List<LessonUnit> dividedLessonBlocks = new ArrayList<>();

        for (LessonUnit lessonBlock : lessonBlocks) {
            double remainingBlockSize = lessonBlock.getBlockSize();
            int numberOfBlocks = (int) Math.ceil(lessonBlock.getBlockSize() / blockSize);
            int unitsPerBlock = (int) (blockSize / TimeslotConstant.SIZE_0_5);

            for (int i = 0; i < numberOfBlocks; i++) {
                LessonUnit dividedLessonBlock = new LessonUnit();
                dividedLessonBlock.setId(lessonBlock.getId() + ((long) i * unitsPerBlock));
                dividedLessonBlock.setLesson(lessonBlock.getLesson());
                dividedLessonBlock.setClassroom(lessonBlock.getClassroom());
                dividedLessonBlock.setIsPinned(lessonBlock.getIsPinned());
                dividedLessonBlock.setTimeslot(lessonBlock.getTimeslot());
                dividedLessonBlock.setBlockSize(Math.min(remainingBlockSize, blockSize));

                dividedLessonBlocks.add(dividedLessonBlock);
                remainingBlockSize -= blockSize;
            }
        }

        return dividedLessonBlocks;
    }
}

