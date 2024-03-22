package ipb.pt.timetableapi.mock;

import ipb.pt.timetableapi.mapper.TimeslotMapper;
import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.solver.SizeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class LessonUnitMock {
    private final TimeslotMapper timeslotMapper;

    @Autowired
    public LessonUnitMock(TimeslotMapper timeslotMapper) {
        this.timeslotMapper = timeslotMapper;
    }

    public List<LessonUnit> getLessonUnits(double hoursPerWeek, int blocks) {
        List<LessonUnit> lessonUnits = new ArrayList<>();

        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setHoursPerWeek(hoursPerWeek);
        lesson.setBlocks(blocks);

        Classroom classroom = new Classroom();
        classroom.setId(1L);

        LocalTime startTime = LocalTime.parse("08:00");
        int numberOfUnits = (int) (hoursPerWeek / SizeConstant.SIZE_0_5);

        // FIXME: Improve the timeslot attribution
        int numberOfUnitsPerBlock = numberOfUnits / blocks;
        double timeslotSize = timeslotMapper.getTimeslotSize(hoursPerWeek / blocks);

        for (long i = 1L; i <= numberOfUnits; i++) {
            LocalTime endTime = startTime.plusMinutes(SizeConstant.UNIT_DURATION);
            Timeslot timeslot = new Timeslot(i, DayOfWeek.MONDAY, startTime, endTime);

            LessonUnit lessonUnit = new LessonUnit();
            lessonUnit.setId(i);
            lessonUnit.setLesson(lesson);
            lessonUnit.setTimeslot(timeslot);
            lessonUnit.setClassroom(classroom);
            lessonUnit.setBlockSize(SizeConstant.SIZE_0_5);
            lessonUnit.setIsPinned(false);
            lessonUnits.add(lessonUnit);

            startTime = endTime;
        }

        return lessonUnits;
    }

    public List<LessonUnit> getLessonBlocks(List<Double> blockSizes) {
        List<LessonUnit> lessonBlocks = new ArrayList<>();

        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setBlocks(blockSizes.size());
        lesson.setHoursPerWeek(blockSizes.stream().mapToDouble(Double::doubleValue).sum());

        Classroom classroom = new Classroom();
        classroom.setId(1L);

        LocalTime startTime = LocalTime.parse("08:00");

        long lessonBlockId = 1L;
        long timeslotId = 1L;

        for (Double blockSize : blockSizes) {
            int timeslotUnitsPerBlock = (int) (timeslotMapper.getTimeslotSize(blockSize) / SizeConstant.SIZE_0_5);
            int lessonUnitsPerBlock = (int) (blockSize / SizeConstant.SIZE_0_5);

            LocalTime endTime = startTime.plusMinutes(timeslotUnitsPerBlock * SizeConstant.UNIT_DURATION);
            Timeslot timeslot = new Timeslot(timeslotId, DayOfWeek.MONDAY, startTime, endTime);

            LessonUnit lessonBlock = new LessonUnit();
            lessonBlock.setId(lessonBlockId);
            lessonBlock.setLesson(lesson);
            lessonBlock.setBlockSize(blockSize);
            lessonBlock.setIsPinned(false);
            lessonBlock.setTimeslot(timeslot);
            lessonBlock.setClassroom(classroom);
            lessonBlocks.add(lessonBlock);

            timeslotId += timeslotUnitsPerBlock;
            lessonBlockId += lessonUnitsPerBlock;
            startTime = endTime;
        }

        return lessonBlocks;
    }
}
