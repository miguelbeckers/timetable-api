package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.solver.SizeConstant;
import ipb.pt.timetableapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
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

        LocalTime startTime = lessonBlock.getTimeslot().getStartTime();
        long timeslotId = lessonBlock.getTimeslot().getId();

        for (int i = 0; i < unitsPerBlock; i++) {
            LocalTime endTime = startTime.plusMinutes(SizeConstant.UNIT_DURATION);
            Timeslot timeslot = getTimeslot(timeslotId++, startTime, endTime, lessonBlock.getTimeslot().getDayOfWeek());

            LessonUnit lessonUnit = new LessonUnit();
            lessonUnit.setId(lessonBlock.getId() + i);
            lessonUnit.setBlockSize(SizeConstant.SIZE_0_5);
            lessonUnit.setLesson(lessonBlock.getLesson());
            lessonUnit.setIsPinned(lessonBlock.getIsPinned());
            lessonUnit.setClassroom(lessonBlock.getClassroom());
            lessonUnit.setTimeslot(timeslot);
            lessonUnits.add(lessonUnit);

            startTime = endTime;
        }

        return lessonUnits;
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
            LessonUnit lessonUnit = lessonUnits.get(0);

            LocalTime endTime = startTime.plusMinutes(timeslotUnitsPerBlock * SizeConstant.UNIT_DURATION);
            Timeslot timeslot = getTimeslot(timeslotId, startTime, endTime, lessonUnit.getTimeslot().getDayOfWeek());

            LessonUnit lessonBlock = new LessonUnit();
            lessonBlock.setLesson(lesson);
            lessonBlock.setBlockSize(blockSize);
            lessonBlock.setId(lessonUnit.getId());
            lessonBlock.setIsPinned(lessonUnit.getIsPinned());
            lessonBlock.setClassroom(lessonUnit.getClassroom());
            lessonBlock.setTimeslot(timeslot);
            lessonBlocksForLesson.add(lessonBlock);

            if (lessonUnitsPerBlock > 0) {
                lessonUnits.subList(0, lessonUnitsPerBlock).clear();
            }

            timeslotId += timeslotUnitsPerBlock;
            startTime = endTime;
        }

        return lessonBlocksForLesson;
    }

    public List<LessonUnit> mapBlocksToBlocks(List<LessonUnit> lessonBlocks, double blocksSize) {
        List<LessonUnit> splitLessonBlocks = new ArrayList<>();

        for (LessonUnit lessonBlock : lessonBlocks) {
            splitLessonBlocks.addAll(splitBlock(lessonBlock, blocksSize));
        }

        return splitLessonBlocks;
    }

    private List<LessonUnit> splitBlock(LessonUnit lessonBlock, double blockSize) {
        List<LessonUnit> splitLessonBlocks = new ArrayList<>();

        int timeslotUnitsPerBlock = (int) (timeslotMapper.getTimeslotSize(blockSize) / SizeConstant.SIZE_0_5);
        int numberOfBlocks = (int) Math.ceil(lessonBlock.getBlockSize() / blockSize);
        double remainingSize = lessonBlock.getBlockSize();

        LocalTime startTime = lessonBlock.getTimeslot().getStartTime();
        long timeslotId = lessonBlock.getTimeslot().getId();

        for (int i = 0; i < numberOfBlocks; i++) {
            LocalTime endTime = startTime.plusMinutes(timeslotUnitsPerBlock * SizeConstant.UNIT_DURATION);
            Timeslot timeslot = getTimeslot(timeslotId, startTime, endTime, lessonBlock.getTimeslot().getDayOfWeek());
            long lessonBlockId = (long) (lessonBlock.getId() + (blockSize / SizeConstant.SIZE_0_5) * i);

            LessonUnit newLessonBlock = new LessonUnit();
            newLessonBlock.setId(lessonBlockId);
            newLessonBlock.setLesson(lessonBlock.getLesson());
            newLessonBlock.setBlockSize(Math.min(blockSize, remainingSize));
            newLessonBlock.setIsPinned(lessonBlock.getIsPinned());
            newLessonBlock.setClassroom(lessonBlock.getClassroom());
            newLessonBlock.setTimeslot(timeslot);
            splitLessonBlocks.add(newLessonBlock);

            remainingSize -= blockSize;
            timeslotId += timeslotUnitsPerBlock;
            startTime = endTime;
        }

        return splitLessonBlocks;
    }

    private Timeslot getTimeslot(long timeslotId, LocalTime startTime, LocalTime endTime, DayOfWeek dayOfWeek) {
        Timeslot timeslot = new Timeslot();
        timeslot.setId(timeslotId);
        timeslot.setDayOfWeek(dayOfWeek);
        timeslot.setStartTime(startTime);
        timeslot.setEndTime(endTime);
        return timeslot;
    }

    public List<LessonUnit> getLessonBlocksBySize(List<LessonUnit> lessonUnits, double size, Double nextSize, Double firstSize) {
        List<LessonUnit> lessonBlocks = mapUnitsToBlocks(lessonUnits);
        List<LessonUnit> lessonBlocksOfTheCurrentSize = getLessonBlocksBySize(lessonBlocks, size, nextSize);

        if (firstSize == size) {
            return lessonBlocksOfTheCurrentSize;
        }

        List<LessonUnit> lessonBlocksOfThePreviousSize = getLessonBlocksBySize(lessonBlocks, firstSize, size);
        List<LessonUnit> previousLessonBlocksSplitIntoTheCurrentSize = mapBlocksToBlocks(lessonBlocksOfThePreviousSize, size);

        return new ArrayList<>() {{
            addAll(lessonBlocksOfTheCurrentSize);
            addAll(previousLessonBlocksSplitIntoTheCurrentSize);
        }};
    }

    private List<LessonUnit> getLessonBlocksBySize(List<LessonUnit> lessonBlocks, double size, Double nextSize) {
        return lessonBlocks.stream().filter(lessonUnit -> lessonUnit.getBlockSize() <= size
                && (nextSize == null || lessonUnit.getBlockSize() > nextSize)).toList();
    }
}
