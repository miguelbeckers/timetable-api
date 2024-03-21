package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.solver.SizeConstant;
import ipb.pt.timetableapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class LessonUnitMapper {
    private final TimeslotMapper timeslotMapper;

    @Autowired
    public LessonUnitMapper(TimeslotMapper timeslotMapper) {
        this.timeslotMapper = timeslotMapper;
    }

    public List<LessonUnit> mapBlocksToUnits(List<LessonUnit> lessonBlocks) {
        List<LessonUnit> lessonUnits = new ArrayList<>();
        lessonBlocks.forEach(lessonBlock -> lessonUnits.addAll(splitABlockIntoUnits(lessonBlock)));
        return lessonUnits;
    }

    private List<LessonUnit> splitABlockIntoUnits(LessonUnit lessonBlock) {
        List<LessonUnit> lessonUnits = new ArrayList<>();
        int unitsPerBlock = (int) (lessonBlock.getBlockSize() / SizeConstant.SIZE_0_5);

        for (int i = 0; i < unitsPerBlock; i++) {
            Timeslot timeslot = createTimeslot(lessonBlock, i);

            LessonUnit lessonUnit = new LessonUnit();
            lessonUnit.setId(lessonBlock.getId() + i);
            lessonUnit.setBlockSize(SizeConstant.SIZE_0_5);
            lessonUnit.setLesson(lessonBlock.getLesson());
            lessonUnit.setIsPinned(lessonBlock.getIsPinned());
            lessonUnit.setClassroom(lessonBlock.getClassroom());
            lessonUnit.setTimeslot(timeslot);

            lessonUnits.add(lessonUnit);
        }

        return lessonUnits;
    }

    private static Timeslot createTimeslot(LessonUnit lessonBlock, int i) {
        long additionalStart = i * SizeConstant.UNIT_DURATION;
        LocalTime startTime = lessonBlock.getTimeslot().getStartTime().plusMinutes(additionalStart);
        LocalTime endTime = startTime.plusMinutes(SizeConstant.UNIT_DURATION);

        Timeslot timeslot = new Timeslot();
        timeslot.setId(lessonBlock.getTimeslot().getId() + i);
        timeslot.setDayOfWeek(lessonBlock.getTimeslot().getDayOfWeek());
        timeslot.setStartTime(startTime);
        timeslot.setEndTime(endTime);
        return timeslot;
    }

    public List<LessonUnit> mapUnitsToBlocks(List<LessonUnit> lessonUnits) {
        List<LessonUnit> lessonBlocks = new ArrayList<>();

        HashMap<Lesson, List<LessonUnit>> lessonUnitsMap = new HashMap<>();
        lessonUnits.forEach(lessonUnit -> groupLessonUnitsByLesson(lessonUnitsMap, lessonUnit));

        for (Lesson lesson : lessonUnitsMap.keySet()) {
            List<LessonUnit> lessonUnitsWithSameLesson = lessonUnitsMap.get(lesson);
            List<LessonUnit> lessonBlocksForLesson = mergeUnitsIntoBlocks(lessonUnitsWithSameLesson, lesson);
            lessonBlocks.addAll(lessonBlocksForLesson);
        }

        return lessonBlocks;
    }

    private void groupLessonUnitsByLesson(HashMap<Lesson, List<LessonUnit>> lessonBlocksMap, LessonUnit lessonBlock) {
        Lesson lesson = lessonBlock.getLesson();
        if (lessonBlocksMap.containsKey(lesson)) {
            List<LessonUnit> lessonBlocksForLesson = lessonBlocksMap.get(lesson);
            lessonBlocksForLesson.add(lessonBlock);
        } else {
            List<LessonUnit> lessonBlocksForLesson = new ArrayList<>();
            lessonBlocksForLesson.add(lessonBlock);
            lessonBlocksMap.put(lesson, lessonBlocksForLesson);
        }
    }

    private List<LessonUnit> mergeUnitsIntoBlocks(List<LessonUnit> lessonUnits, Lesson lesson) {
        double blockSize = (double) lesson.getHoursPerWeek() / lesson.getBlocks();
        return new ArrayList<>(createBlocks(lessonUnits, lesson, blockSize));
    }

    private List<LessonUnit> createBlocks(List<LessonUnit> lessonUnits, Lesson lesson, double blockSize) {
        List<LessonUnit> lessonBlocksForLesson = new ArrayList<>();

        int timeslotUnitsPerBlock = (int) (timeslotMapper.getTimeslotSize(blockSize) / SizeConstant.SIZE_0_5);
        int lessonUnitsPerBlock = (int) (blockSize / SizeConstant.SIZE_0_5);

        LocalTime startTime = lessonUnits.get(0).getTimeslot().getStartTime();
        long timeslotId = lessonUnits.get(0).getTimeslot().getId();

        for (int i = 0; i < lesson.getBlocks(); i++) {
            LessonUnit currentLessonUnit = lessonUnits.get(0);

            LocalTime endTime = startTime.plusMinutes(timeslotUnitsPerBlock * SizeConstant.UNIT_DURATION);

            Timeslot timeslot = new Timeslot();
            timeslot.setId(timeslotId);
            timeslot.setDayOfWeek(currentLessonUnit.getTimeslot().getDayOfWeek());
            timeslot.setStartTime(startTime);
            timeslot.setEndTime(endTime);

            LessonUnit lessonBlock = new LessonUnit();
            lessonBlock.setLesson(lesson);
            lessonBlock.setBlockSize(blockSize);
            lessonBlock.setId(currentLessonUnit.getId());
            lessonBlock.setIsPinned(currentLessonUnit.getIsPinned());
            lessonBlock.setClassroom(currentLessonUnit.getClassroom());
            lessonBlock.setTimeslot(timeslot);
            lessonBlocksForLesson.add(lessonBlock);

            lessonUnits.subList(0, lessonUnitsPerBlock).clear();

            timeslotId += timeslotUnitsPerBlock;
            startTime = endTime;
        }

        return lessonBlocksForLesson;
    }

    public List<LessonUnit> mapBlocksToBlocks(List<LessonUnit> lessonBlocks, double blocksSize) {
        List<LessonUnit> newLessonBlocks = new ArrayList<>();

        HashMap<Lesson, List<LessonUnit>> lessonBlocksMap = new HashMap<>();
        lessonBlocks.forEach(lessonBlock -> groupLessonUnitsByLesson(lessonBlocksMap, lessonBlock));

        for (Lesson lesson : lessonBlocksMap.keySet()) {
            List<LessonUnit> lessonBlocksForLesson = lessonBlocksMap.get(lesson);
            List<LessonUnit> lessonBlocksForLessonWithSize = createBlocks(lessonBlocksForLesson, lesson, blocksSize);
            newLessonBlocks.addAll(lessonBlocksForLessonWithSize);
        }

        return newLessonBlocks;
    }

    public List<LessonUnit> getLessonBlocksBySize(List<LessonUnit> lessonUnits, double currentSize, Double nextSize, Double firstSize) {
        List<LessonUnit> lessonBlocks = mapUnitsToBlocks(lessonUnits);
        List<LessonUnit> lessonBlocksOfTheCurrentSize = getLessonBlocksBySize(lessonBlocks, currentSize, nextSize);

        if (firstSize == currentSize) {
            return lessonBlocksOfTheCurrentSize;
        }

        List<LessonUnit> lessonBlocksOfThePreviousSize = getLessonBlocksBySize(lessonBlocks, firstSize, currentSize);
        List<LessonUnit> previousLessonBlocksSplitIntoTheCurrentSize = mapBlocksToBlocks(lessonBlocksOfThePreviousSize, currentSize);

        return new ArrayList<>() {{
            addAll(lessonBlocksOfTheCurrentSize);
            addAll(previousLessonBlocksSplitIntoTheCurrentSize);
        }};
    }

    private List<LessonUnit> getLessonBlocksBySize(List<LessonUnit> lessonBlocks, double currentSize, Double nextSize) {
        return lessonBlocks.stream().filter(lessonUnit -> lessonUnit.getBlockSize() <= currentSize
                && (nextSize == null || lessonUnit.getBlockSize() > nextSize)).toList();
    }
}
