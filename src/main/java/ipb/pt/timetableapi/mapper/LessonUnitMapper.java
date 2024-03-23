package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.solver.SizeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

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

            Optional<Timeslot> timeslotOptional = Optional.ofNullable(lessonBlock.getTimeslot());
            Long timeslotId = timeslotOptional.map(Timeslot::getId).orElse(null);
            LocalTime startTime = timeslotOptional.map(Timeslot::getStartTime).orElse(null);
            DayOfWeek dayOfWeek = timeslotOptional.map(Timeslot::getDayOfWeek).orElse(null);

            for (int i = 0; i < unitsPerBlock; i++) {
                Timeslot timeslot = null;

                if (timeslotId != null) {
                    LocalTime endTime = startTime.plusMinutes(SizeConstant.UNIT_DURATION);
                    timeslot = new Timeslot(timeslotId++, dayOfWeek, startTime, endTime);
                    startTime = endTime;
                }

                LessonUnit lessonUnit = new LessonUnit();
                lessonUnit.setId(lessonBlock.getId() + i);
                lessonUnit.setBlockSize(SizeConstant.SIZE_0_5);
                lessonUnit.setLesson(lessonBlock.getLesson());
                lessonUnit.setIsPinned(lessonBlock.getIsPinned());
                lessonUnit.setClassroom(lessonBlock.getClassroom());
                lessonUnit.setTimeslot(timeslot);
                lessonUnits.add(lessonUnit);
            }
        }

        lessonUnitChecker(lessonUnits);
        return lessonUnits;
    }

    public static void lessonUnitChecker(List<LessonUnit> lessonBlocks) {
        System.out.println("##########################################################################################");

        HashMap<Lesson, List<LessonUnit>> lessonUnitMap = new HashMap<>();
        for (LessonUnit lessonBlock : lessonBlocks) {
            Lesson lesson = lessonBlock.getLesson();
            lessonUnitMap.computeIfAbsent(lesson, k -> new ArrayList<>()).add(lessonBlock);
        }

        for (Map.Entry<Lesson, List<LessonUnit>> entry : lessonUnitMap.entrySet()) {
            List<LessonUnit> lessonBlocksWithSameLesson = entry.getValue();
            Lesson lesson = entry.getKey();
            boolean isTheRightNumber = lessonBlocksWithSameLesson.size() == lesson.getHoursPerWeek() / SizeConstant.SIZE_0_5;

            System.out.println("--------------------------------------------------------------------------------------");
            System.out.println("lesson: " + lesson.getId() +
                    " | hoursPerWeek: " + lesson.getHoursPerWeek() +
                    " | blocks: " + lesson.getBlocks() +
                    " | created blocks: " + lessonBlocksWithSameLesson.size() +
                    " | isPinned: " + lessonBlocksWithSameLesson.get(0).getIsPinned() +
                    " | isTheRightNumber: " + isTheRightNumber);

            for (LessonUnit lessonBlock : lessonBlocksWithSameLesson) {
                Optional<Timeslot> timeslotOptional = Optional.ofNullable(lessonBlock.getTimeslot());
                DayOfWeek dayOfWeek = timeslotOptional.map(Timeslot::getDayOfWeek).orElse(null);
                LocalTime startTime = timeslotOptional.map(Timeslot::getStartTime).orElse(null);
                LocalTime endTime = timeslotOptional.map(Timeslot::getEndTime).orElse(null);

                Optional<Classroom> classroomOptional = Optional.ofNullable(lessonBlock.getClassroom());
                Long classroomId = classroomOptional.map(Classroom::getId).orElse(null);

                System.out.println("lessonBlockId: " + lessonBlock.getId() +
                        " | blockSize: " + lessonBlock.getBlockSize() +
                        " | dayOfWeek: " + dayOfWeek +
                        " | startTime: " + startTime +
                        " | endTime: " + endTime +
                        " | classroomId: " + classroomId);
            }
        }
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

            for (int i = 0; i < lesson.getBlocks(); i++) {
                LessonUnit firstLessonUnit = lessonUnitsWithSameLesson.get(0);
                Timeslot timeslot = null;

                if (firstLessonUnit.getTimeslot() != null) {
                    long timeslotId = firstLessonUnit.getTimeslot().getId();
                    long blockDuration = timeslotUnitsPerBlock * SizeConstant.UNIT_DURATION;

                    LocalTime startTime = firstLessonUnit.getTimeslot().getStartTime();
                    LocalTime endTime = startTime.plusMinutes(blockDuration);
                    DayOfWeek dayOfWeek = firstLessonUnit.getTimeslot().getDayOfWeek();

                    timeslot = new Timeslot(timeslotId, dayOfWeek, startTime, endTime);
                }

                LessonUnit lessonBlock = new LessonUnit();
                lessonBlock.setLesson(lesson);
                lessonBlock.setBlockSize(blockSize);
                lessonBlock.setId(firstLessonUnit.getId());
                lessonBlock.setIsPinned(firstLessonUnit.getIsPinned());
                lessonBlock.setClassroom(firstLessonUnit.getClassroom());
                lessonBlock.setTimeslot(timeslot);
                lessonBlocks.add(lessonBlock);

                if (lessonUnitsPerBlock > 0) {
                    lessonUnitsWithSameLesson.subList(0, lessonUnitsPerBlock).clear();
                }
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

            Optional<Timeslot> timeslotOptional = Optional.ofNullable(lessonBlock.getTimeslot());
            Long timeslotId = timeslotOptional.map(Timeslot::getId).orElse(null);
            LocalTime startTime = timeslotOptional.map(Timeslot::getStartTime).orElse(null);
            DayOfWeek dayOfWeek = timeslotOptional.map(Timeslot::getDayOfWeek).orElse(null);

            for (int i = 0; i < numberOfBlocks; i++) {
                Timeslot timeslot = null;

                if (timeslotId != null) {
                    long duration = timeslotUnitsPerBlock * SizeConstant.UNIT_DURATION;
                    LocalTime endTime = startTime.plusMinutes(duration);
                    timeslot = new Timeslot(timeslotId, dayOfWeek, startTime, endTime);

                    timeslotId += timeslotUnitsPerBlock;
                    startTime = endTime;
                }

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
            }
        }

        return splitLessonBlocks;
    }
}
