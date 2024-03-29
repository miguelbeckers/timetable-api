package ipb.pt.timetableapi.mapper.lessonUnitMapper;

import ipb.pt.timetableapi.mapper.LessonUnitMapper;
import ipb.pt.timetableapi.mock.LessonUnitMock;
import ipb.pt.timetableapi.model.LessonUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class MapUnitsToBlocksTest {
    private final LessonUnitMapper lessonUnitMapper;
    private final LessonUnitMock lessonUnitMock;

    @Autowired
    public MapUnitsToBlocksTest(LessonUnitMapper lessonUnitMapper, LessonUnitMock lessonUnitMock) {
        this.lessonUnitMapper = lessonUnitMapper;
        this.lessonUnitMock = lessonUnitMock;
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(5, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(4.5, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(4, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(3.5, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(3, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(2.5, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(2, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(1.5, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(1, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(0.5, 1);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(5, 2);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(4, 2);
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

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(1, 1);
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

    @Test
    public void testTheCaseOfTheFirstUnitOfTheFirstBlockWithoutTimeslot() {
        // id | time           | input       | output
        // 01 | null           | ┌── 0.5 ──┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        // 05 | 10:00 -> 10:30 | ┌── 0.5 ──┐ | └─────────┘
        // 06 | 10:30 -> 11:00 | ┌── 0.5 ──┐ | ┌── 2.5 ──┐
        // 07 | 11:00 -> 11:30 | ┌── 0.5 ──┐ | │         │
        // 08 | 11:30 -> 12:00 | ┌── 0.5 ──┐ | │         │
        // 09 | 12:00 -> 12:30 | ┌── 0.5 ──┐ | │         │
        // 10 | 12:30 -> 13:00 | ┌── 0.5 ──┐ | └─────────┘

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(5, 2);
        lessonUnits.get(0).setTimeslot(null);

        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 2,
                "The number of blocks is not 2");

        Assert.isTrue(lessonBlocks.get(0).getId() == 1,
                "The id of the lessonBlock 0 is not 1");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot() == null,
                "The timeslot of the lessonBlock 0 is not null");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot().getId() == 6,
                "The id of the timeslot of the lessonBlock 1 is not 6");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
                "The startTime of the lessonBlock 1 is not 10:30");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The endTime of the lessonBlock 1 is not 13:00");
    }

    @Test
    public void testTheCaseOfTheFirstUnitOfTheSecondBlockWithoutTimeslot() {
        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | ┌── 0.5 ──┐ | │         │
        // 03 | 09:00 -> 09:30 | ┌── 0.5 ──┐ | │         │
        // 04 | 09:30 -> 10:00 | ┌── 0.5 ──┐ | │         │
        // 05 | 10:00 -> 10:30 | ┌── 0.5 ──┐ | └─────────┘
        // 06 | null           | ┌── 0.5 ──┐ | ┌── 2.5 ──┐
        // 07 | 11:00 -> 11:30 | ┌── 0.5 ──┐ | │         │
        // 08 | 11:30 -> 12:00 | ┌── 0.5 ──┐ | │         │
        // 09 | 12:00 -> 12:30 | ┌── 0.5 ──┐ | │         │
        // 10 | 12:30 -> 13:00 | ┌── 0.5 ──┐ | └─────────┘

        List<LessonUnit> lessonUnits = lessonUnitMock.getLessonUnits(5, 2);
        lessonUnits.get(5).setTimeslot(null);

        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);

        Assert.isTrue(lessonBlocks.size() == 2,
                "The number of blocks is not 2");

        Assert.isTrue(lessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The timeslot of the lessonBlock 0 doesn't start at 08:00");

        Assert.isTrue(lessonBlocks.get(1).getTimeslot() == null,
                "The timeslot of the lessonBlock 1 is not null");
    }
}
