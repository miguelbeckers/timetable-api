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
import java.util.Map;

@Component
public class LessonUnitMapper {
    private final TimeslotMapper timeslotMapper;

    @Autowired
    public LessonUnitMapper(TimeslotMapper timeslotMapper) {
        this.timeslotMapper = timeslotMapper;
    }

    public List<LessonUnit> mapBlocksToUnits(List<LessonUnit> lessonBlocks) {
        List<LessonUnit> lessonUnits = new ArrayList<>();

        for (LessonUnit lessonBlock : lessonBlocks) {
            int unitsPerBlock = (int) (lessonBlock.getBlockSize() / SizeConstant.SIZE_0_5);
            long timeslotId = lessonBlock.getTimeslot().getId();

            LocalTime startTime = lessonBlock.getTimeslot().getStartTime();
            DayOfWeek dayOfWeek = lessonBlock.getTimeslot().getDayOfWeek();

            for (int i = 0; i < unitsPerBlock; i++) {
                LocalTime endTime = startTime.plusMinutes(SizeConstant.UNIT_DURATION);
                Timeslot timeslot = new Timeslot(timeslotId++, dayOfWeek, startTime, endTime);

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
        }

        return lessonUnits;
    }

    public List<LessonUnit> mapUnitsToBlocks(List<LessonUnit> lessonUnits) {
        List<LessonUnit> lessonBlocks = new ArrayList<>();
        HashMap<Lesson, List<LessonUnit>> lessonBlocksMap = new HashMap<>();

        for (LessonUnit lessonUnit : lessonUnits) {
            Lesson lesson = lessonUnit.getLesson();
            lessonBlocksMap.computeIfAbsent(lesson, k -> new ArrayList<>()).add(lessonUnit);
        }

        for (Map.Entry<Lesson, List<LessonUnit>> entry : lessonBlocksMap.entrySet()) {
            List<LessonUnit> lessonUnitsWithSameLesson = entry.getValue();
            Lesson lesson = entry.getKey();

            double blockSize = (double) lesson.getHoursPerWeek() / lesson.getBlocks();
            int timeslotUnitsPerBlock = (int) (timeslotMapper.getTimeslotSize(blockSize) / SizeConstant.SIZE_0_5);
            int lessonUnitsPerBlock = (int) (blockSize / SizeConstant.SIZE_0_5);

            long timeslotId = lessonUnitsWithSameLesson.get(0).getTimeslot().getId();
            LocalTime startTime = lessonUnitsWithSameLesson.get(0).getTimeslot().getStartTime();
            DayOfWeek dayOfWeek = lessonUnitsWithSameLesson.get(0).getTimeslot().getDayOfWeek();

            for (int i = 0; i < lesson.getBlocks(); i++) {
                LessonUnit lessonUnit = lessonUnitsWithSameLesson.get(0);

                LocalTime endTime = startTime.plusMinutes(timeslotUnitsPerBlock * SizeConstant.UNIT_DURATION);
                Timeslot timeslot = new Timeslot(timeslotId, dayOfWeek, startTime, endTime);

                LessonUnit lessonBlock = new LessonUnit();
                lessonBlock.setLesson(lesson);
                lessonBlock.setBlockSize(blockSize);
                lessonBlock.setId(lessonUnit.getId());
                lessonBlock.setIsPinned(lessonUnit.getIsPinned());
                lessonBlock.setClassroom(lessonUnit.getClassroom());
                lessonBlock.setTimeslot(timeslot);
                lessonBlocks.add(lessonBlock);

                if (lessonUnitsPerBlock > 0) {
                    lessonUnitsWithSameLesson.subList(0, lessonUnitsPerBlock).clear();
                }

                timeslotId += timeslotUnitsPerBlock;
                startTime = endTime;
            }
        }

        return lessonBlocks;
    }

    public List<LessonUnit> mapBlocksToBlocks(List<LessonUnit> lessonBlocks, double blockSize) {
        List<LessonUnit> splitLessonBlocks = new ArrayList<>();
        int timeslotUnitsPerBlock = (int) (timeslotMapper.getTimeslotSize(blockSize) / SizeConstant.SIZE_0_5);

        for (LessonUnit lessonBlock : lessonBlocks) {
            int numberOfBlocks = (int) Math.ceil(lessonBlock.getBlockSize() / blockSize);
            double remainingSize = lessonBlock.getBlockSize();

            long timeslotId = lessonBlock.getTimeslot().getId();
            LocalTime startTime = lessonBlock.getTimeslot().getStartTime();
            DayOfWeek dayOfWeek = lessonBlock.getTimeslot().getDayOfWeek();

            for (int i = 0; i < numberOfBlocks; i++) {
                LocalTime endTime = startTime.plusMinutes(timeslotUnitsPerBlock * SizeConstant.UNIT_DURATION);
                Timeslot timeslot = new Timeslot(timeslotId, dayOfWeek, startTime, endTime);
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
        }

        return splitLessonBlocks;
    }
}
