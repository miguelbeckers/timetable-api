package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.solver.SizeConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MapUnitsToBlocksTest {
    private final LessonUnitMapper lessonUnitMapper;

    @Autowired
    public MapUnitsToBlocksTest(LessonUnitMapper lessonUnitMapper) {
        this.lessonUnitMapper = lessonUnitMapper;
    }

    @Test
    public void test_10_Units_To_1_Block_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌─── 5 ───┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        // 05 | 10:00 -> 10:30 | ┌── 0.5 ──┐ | │         │
        // 06 | 10:30 -> 11:00 | ┌── 0.5 ──┐ | │         │
        // 07 | 11:00 -> 11:30 | ┌── 0.5 ──┐ | │         │
        // 08 | 11:30 -> 12:00 | ┌── 0.5 ──┐ | │         │
        // 09 | 12:00 -> 12:30 | ┌── 0.5 ──┐ | │         │
        // 10 | 12:30 -> 13:00 | ┌── 0.5 ──┐ | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(5, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The endTime of the lessonBlock 0 is not 13:00");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 5,
                "The blockSize of the lessonBlock 0 is not 5");
    }

    @Test
    public void test_9_Units_To_1_Block_4_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌── 4.5 ──┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        // 05 | 10:00 -> 10:30 | ┌── 0.5 ──┐ | │         │
        // 06 | 10:30 -> 11:00 | ┌── 0.5 ──┐ | │         │
        // 07 | 11:00 -> 11:30 | ┌── 0.5 ──┐ | │         │
        // 08 | 11:30 -> 12:00 | ┌── 0.5 ──┐ | │         │
        // 09 | 12:00 -> 12:30 | ┌── 0.5 ──┐ | │         │
        //    | 12:30 -> 13:00 |             | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(4.5, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The endTime of the lessonBlock 0 is not 13:00");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 4.5,
                "The blockSize of the lessonBlock 0 is not 4.5");
    }

    @Test
    public void test_8_Units_To_1_Block_4() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌─── 4 ───┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        // 05 | 10:00 -> 10:30 | ┌── 0.5 ──┐ | │         │
        // 06 | 10:30 -> 11:00 | ┌── 0.5 ──┐ | │         │
        // 07 | 11:00 -> 11:30 | ┌── 0.5 ──┐ | │         │
        // 08 | 11:30 -> 12:00 | ┌── 0.5 ──┐ | │         │
        //    | 12:00 -> 12:30 |             | │         │
        //    | 12:30 -> 13:00 |             | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(4, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The endTime of the lessonBlock 0 is not 13:00");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 4,
                "The blockSize of the lessonBlock 0 is not 4");
    }

    @Test
    public void test_7_Units_To_1_Block_3_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌── 3.5 ──┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        // 05 | 10:00 -> 10:30 | ┌── 0.5 ──┐ | │         │
        // 06 | 10:30 -> 11:00 | ┌── 0.5 ──┐ | │         │
        // 07 | 11:00 -> 11:30 | ┌── 0.5 ──┐ | │         │
        //    | 11:30 -> 12:00 |             | │         │
        //    | 12:00 -> 12:30 |             | │         │
        //    | 12:30 -> 13:00 |             | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(3.5, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The endTime of the lessonBlock 0 is not 13:00");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 3.5,
                "The blockSize of the lessonBlock 0 is not 3.5");
    }

    @Test
    public void test_6_Units_To_1_Block_3() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌─── 3 ───┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        // 05 | 10:00 -> 10:30 | ┌── 0.5 ──┐ | │         │
        // 06 | 10:30 -> 11:00 | ┌── 0.5 ──┐ | │         │
        //    | 11:00 -> 11:30 |             | │         │
        //    | 11:30 -> 12:00 |             | │         │
        //    | 12:00 -> 12:30 |             | │         │
        //    | 12:30 -> 13:00 |             | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(3, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The endTime of the lessonBlock 0 is not 13:00");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 3,
                "The blockSize of the lessonBlock 0 is not 3");
    }

    @Test
    public void test_5_Units_To_1_Block_2_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        // 05 | 10:00 -> 10:30 | ┌── 0.5 ──┐ | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(2.5, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The endTime of the lessonBlock 0 is not 10:30");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 2.5,
                "The blockSize of the lessonBlock 0 is not 2.5");
    }

    @Test
    public void test_4_Units_To_1_Block_2() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌─── 2 ───┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        //    | 10:00 -> 10:30 |             | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(2, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The endTime of the lessonBlock 0 is not 10:30");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 2,
                "The blockSize of the lessonBlock 0 is not 2");
    }

    @Test
    public void test_3_Units_To_1_Block_1_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌── 1.5 ──┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        //    | 09:30 -> 10:00 |             | │         │
        //    | 10:00 -> 10:30 |             | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(1.5, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The endTime of the lessonBlock 0 is not 10:30");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 1.5,
                "The blockSize of the lessonBlock 0 is not 1.5");
    }

    @Test
    public void test_2_Units_To_1_Block_1() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌─── 1 ───┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        //    | 09:00 -> 09:30 |             | │         │
        //    | 09:30 -> 10:00 |             | │         │
        //    | 10:00 -> 10:30 |             | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(1, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The endTime of the lessonBlock 0 is not 10:30");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 1,
                "The blockSize of the lessonBlock 0 is not 1");
    }

    @Test
    public void test_1_Units_To_1_Block_0_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌── 0.5 ──┐

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(0.5, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("08:30")),
                "The endTime of the lessonBlock 0 is not 08:30");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 0.5,
                "The blockSize of the lessonBlock 0 is not 0.5");
    }

    @Test
    public void test_10_Units_To_2_Blocks_2_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        // 05 | 10:00 -> 10:30 | ┌── 0.5 ──┐ | └─────────┘
        // 06 | 10:30 -> 11:00 | ┌── 0.5 ──┐ | ┌── 2.5 ──┐
        // 07 | 11:00 -> 11:30 | ┌── 0.5 ──┐ | │         │
        // 08 | 11:30 -> 12:00 | ┌── 0.5 ──┐ | │         │
        // 09 | 12:00 -> 12:30 | ┌── 0.5 ──┐ | │         │
        // 10 | 12:30 -> 13:00 | ┌── 0.5 ──┐ | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(5, 2);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 2,
                "The number of blocks is not 2");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The endTime of the lessonBlock 0 is not 10:30");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 2.5,
                "The blockSize of the lessonBlock 0 is not 2.5");

        Assert.isTrue(lessonBlocks.get(1).getId() == 6,
                "The id of the lessonBlock 1 is not 6");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot().getId() == 6,
                "The id of the timeslot of the lessonBlock 1 is not 6");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
                "The startTime of the lessonBlock 1 is not 10:30");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The endTime of the lessonBlock 1 is not 13:00");

        Assert.isTrue(lessonBlocks.get(1).getBlockSize() == 2.5,
                "The blockSize of the lessonBlock 1 is not 2.5");
    }

    @Test
    public void test_8_Units_To_2_Blocks_2() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌─── 2 ───┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        //    | 10:00 -> 10:30 |             | └─────────┘
        // 05 | 10:30 -> 11:00 | ┌── 0.5 ──┐ | ┌─── 2 ───┐
        // 06 | 11:00 -> 11:30 | ┌── 0.5 ──┐ | │         │
        // 07 | 11:30 -> 12:00 | ┌── 0.5 ──┐ | │         │
        // 08 | 12:00 -> 12:30 | ┌── 0.5 ──┐ | │         │
        //    | 12:30 -> 13:00 |             | └─────────┘

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(4, 2);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 2,
                "The number of blocks is not 2");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The startTime of the lessonBlock 0 is not 08:00");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The endTime of the lessonBlock is not 10:30");

        Assert.isTrue(lessonBlocks.get(0).getBlockSize() == 2,
                "The blockSize of the lessonBlock 0 is not 2");

        Assert.isTrue(lessonBlocks.get(1).getId() == 5,
                "The id of the lessonBlock 1 is not 5");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot().getId() == 6,
                "The id of the timeslot of the lessonBlock 1 is not 6");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
                "The startTime of the lessonBlock 1 is not 10:30");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The endTime of the lessonBlock 1 is not 13:00");

        Assert.isTrue(lessonBlocks.get(1).getBlockSize() == 2,
                "The blockSize of the lessonBlock 1 is not 2");
    }

    @Test
    public void testIfAllThePropertiesArBeingAssigned() {

        List<LessonUnit> lessonUnits = getLessonUnitsOfALesson(1, 1);
        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 1,
                "The number of blocks is not 1");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getId() == 1,
                "The id of the timeslot of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getClassroom().getId() == 1,
                "The id of the classroom of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getLesson().getId() == 1,
                "The id of the lesson of the lessonBlock 0 is not 1");

        Assert.isTrue(!lessonBlocks.get(0).getIsPinned(),
                "The isPinned of the lessonBlock 0 is not false");
    }

    private List<LessonUnit> getLessonUnitsOfALesson(double hoursPerWeek, int blocks) {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setHoursPerWeek(hoursPerWeek);
        lesson.setBlocks(blocks);

        Classroom classroom = new Classroom();
        classroom.setId(1L);

        int numberOfUnits = (int) (hoursPerWeek / SizeConstant.SIZE_0_5);

        List<LessonUnit> lessonUnits = new ArrayList<>();
        for (int i = 0; i < numberOfUnits; i++) {
            long additionalStart = i * SizeConstant.UNIT_DURATION;
            LocalTime startTime = LocalTime.parse("08:00").plusMinutes(additionalStart);
            LocalTime endTime = startTime.plusMinutes(SizeConstant.UNIT_DURATION);

            Timeslot timeslot = new Timeslot();
            timeslot.setId((long) i + 1);
            timeslot.setDayOfWeek(DayOfWeek.MONDAY);
            timeslot.setStartTime(startTime);
            timeslot.setEndTime(endTime);

            LessonUnit lessonUnit = new LessonUnit();
            lessonUnit.setId((long) i + 1);
            lessonUnit.setLesson(lesson);
            lessonUnit.setTimeslot(timeslot);
            lessonUnit.setClassroom(classroom);
            lessonUnit.setBlockSize(SizeConstant.SIZE_0_5);
            lessonUnit.setIsPinned(false);

            lessonUnits.add(lessonUnit);
        }

        return lessonUnits;
    }
}
