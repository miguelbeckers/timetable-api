package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.mock.LessonUnitMock;
import ipb.pt.timetableapi.model.LessonUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class MapBlocksToBlocksTest {
    private final LessonUnitMapper lessonUnitMapper;
    private final LessonUnitMock lessonUnitMock;

    @Autowired
    public MapBlocksToBlocksTest(LessonUnitMapper lessonUnitMapper, LessonUnitMock lessonUnitMock) {
        this.lessonUnitMapper = lessonUnitMapper;
        this.lessonUnitMock = lessonUnitMock;
    }

    @Test
    public void test_1_Block_5_To_2_Blocks_2_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 5 ───┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | │         │
        // 03 | 09:00 -> 09:30 | │         │ | │         │
        // 04 | 09:30 -> 10:00 | │         │ | │         │
        // 05 | 10:00 -> 10:30 | │         │ | └─────────┘
        // 06 | 10:30 -> 11:00 | │         │ | ┌── 2.5 ──┐
        // 07 | 11:00 -> 11:30 | │         │ | │         │
        // 08 | 11:30 -> 12:00 | │         │ | │         │
        // 09 | 12:00 -> 12:30 | │         │ | │         │
        // 10 | 12:30 -> 13:00 | └─────────┘ | └─────────┘

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(5.0));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 2.5);

        Assert.isTrue(splitLessonBlocks.size() == 2,
                "The splitLessonBlocks should have 2 blocks");

        Assert.isTrue(splitLessonBlocks.get(0).getId() == 1,
                "The first block should have the id 1");

        Assert.isTrue(splitLessonBlocks.get(0).getBlockSize() == 2.5,
                "The first block should have the block size 2.5");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The first block should have the start time 08:00");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The first block should have the end time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getId() == 6,
                "The second block should have the id 6");

        Assert.isTrue(splitLessonBlocks.get(1).getBlockSize() == 2.5,
                "The second block should have the block size 2.5");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
                "The second block should have the start time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The second block should have the end time 13:00");
    }

    @Test
    public void test_1_Block_4_5_To_1_Blocks_2_5_And_1_Bloc_2() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 4.5 ──┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | │         │
        // 03 | 09:00 -> 09:30 | │         │ | │         │
        // 04 | 09:30 -> 10:00 | │         │ | │         │
        // 05 | 10:00 -> 10:30 | │         │ | └─────────┘
        // 06 | 10:30 -> 11:00 | │         │ | ┌─── 2 ───┐
        // 07 | 11:00 -> 11:30 | │         │ | │         │
        // 08 | 11:30 -> 12:00 | │         │ | │         │
        // 09 | 12:00 -> 12:30 | │         │ | │         │
        //    | 12:30 -> 13:00 | └─────────┘ | └─────────┘

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(4.5));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 2.5);

        Assert.isTrue(splitLessonBlocks.size() == 2,
                "The splitLessonBlocks should have 2 blocks");

        Assert.isTrue(splitLessonBlocks.get(0).getId() == 1,
                "The first block should have the id 1");

        Assert.isTrue(splitLessonBlocks.get(0).getBlockSize() == 2.5,
                "The first block should have the block size 2.5");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The first block should have the start time 08:00");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The first block should have the end time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getId() == 6,
                "The second block should have the id 6");

        Assert.isTrue(splitLessonBlocks.get(1).getBlockSize() == 2,
                "The second block should have the block size 2");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
                "The second block should have the start time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The second block should have the end time 13:00");
    }

    @Test
    public void test_1_Block_4_To_1_Blocks_2_5_And_1_Bloc_1_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 4 ───┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | │         │
        // 03 | 09:00 -> 09:30 | │         │ | │         │
        // 04 | 09:30 -> 10:00 | │         │ | │         │
        // 05 | 10:00 -> 10:30 | │         │ | └─────────┘
        // 06 | 10:30 -> 11:00 | │         │ | ┌── 1.5 ──┐
        // 07 | 11:00 -> 11:30 | │         │ | │         │
        // 08 | 11:30 -> 12:00 | │         │ | │         │
        //    | 12:00 -> 12:30 | │         │ | │         │
        //    | 12:30 -> 13:00 | └─────────┘ | └─────────┘

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(4.0));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 2.5);

        Assert.isTrue(splitLessonBlocks.size() == 2,
                "The splitLessonBlocks should have 2 blocks");

        Assert.isTrue(splitLessonBlocks.get(0).getId() == 1,
                "The first block should have the id 1");

        Assert.isTrue(splitLessonBlocks.get(0).getBlockSize() == 2.5,
                "The first block should have the block size 2.5");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The first block should have the start time 08:00");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The first block should have the end time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getId() == 6,
                "The second block should have the id 6");

        Assert.isTrue(splitLessonBlocks.get(1).getBlockSize() == 1.5,
                "The second block should have the block size 1.5");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
                "The second block should have the start time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The second block should have the end time 13:00");
    }

    @Test
    public void test_1_Block_3_5_To_1_Blocks_2_5_And_1_Bloc_1() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 3.5 ──┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | │         │
        // 03 | 09:00 -> 09:30 | │         │ | │         │
        // 04 | 09:30 -> 10:00 | │         │ | │         │
        // 05 | 10:00 -> 10:30 | │         │ | └─────────┘
        // 06 | 10:30 -> 11:00 | │         │ | ┌─── 1 ───┐
        // 07 | 11:00 -> 11:30 | │         │ | │         │
        //    | 11:30 -> 12:00 | │         │ | │         │
        //    | 12:00 -> 12:30 | │         │ | │         │
        //    | 12:30 -> 13:00 | └─────────┘ | └─────────┘

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(3.5));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 2.5);

        Assert.isTrue(splitLessonBlocks.size() == 2,
                "The splitLessonBlocks should have 2 blocks");

        Assert.isTrue(splitLessonBlocks.get(0).getId() == 1,
                "The first block should have the id 1");

        Assert.isTrue(splitLessonBlocks.get(0).getBlockSize() == 2.5,
                "The first block should have the block size 2.5");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The first block should have the start time 08:00");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The first block should have the end time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getId() == 6,
                "The second block should have the id 6");

        Assert.isTrue(splitLessonBlocks.get(1).getBlockSize() == 1,
                "The second block should have the block size 1");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
                "The second block should have the start time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The second block should have the end time 13:00");
    }

    @Test
    public void test_1_Block_3_To_1_Blocks_2_5_And_1_Bloc_0_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 3 ───┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | │         │
        // 03 | 09:00 -> 09:30 | │         │ | │         │
        // 04 | 09:30 -> 10:00 | │         │ | │         │
        // 05 | 10:00 -> 10:30 | │         │ | └─────────┘
        // 06 | 10:30 -> 11:00 | │         │ | ┌── 0.5 ──┐
        // 07 | 11:00 -> 11:30 | │         │ | │         │
        //    | 11:30 -> 12:00 | │         │ | │         │
        //    | 12:00 -> 12:30 | │         │ | │         │
        //    | 12:30 -> 13:00 | └─────────┘ | └─────────┘

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(3.0));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 2.5);

        Assert.isTrue(splitLessonBlocks.size() == 2,
                "The splitLessonBlocks should have 2 blocks");

        Assert.isTrue(splitLessonBlocks.get(0).getId() == 1,
                "The first block should have the id 1");

        Assert.isTrue(splitLessonBlocks.get(0).getBlockSize() == 2.5,
                "The first block should have the block size 2.5");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The first block should have the start time 08:00");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The first block should have the end time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getId() == 6,
                "The second block should have the id 6");

        Assert.isTrue(splitLessonBlocks.get(1).getBlockSize() == 0.5,
                "The second block should have the block size 0.5");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
                "The second block should have the start time 10:30");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The second block should have the end time 13:00");
    }

    @Test
    public void test_1_Block_2_5_To_1_Blocks_2_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 2.5 ──┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | │         │
        // 03 | 09:00 -> 09:30 | │         │ | │         │
        // 04 | 09:30 -> 10:00 | │         │ | │         │
        // 05 | 10:00 -> 10:30 | └─────────┘ | └─────────┘

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(2.5));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 2.5);

        Assert.isTrue(splitLessonBlocks.size() == 1,
                "The splitLessonBlocks should have 1 block");

        Assert.isTrue(splitLessonBlocks.get(0).getId() == 1,
                "The first block should have the id 1");

        Assert.isTrue(splitLessonBlocks.get(0).getBlockSize() == 2.5,
                "The first block should have the block size 2.5");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The first block should have the start time 08:00");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The first block should have the end time 10:30");
    }

    @Test
    public void test_1_Block_2_To_1_Blocks_2_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 2 ───┐ | ┌─── 2 ───┐
        // 02 | 08:30 -> 09:00 | │         │ | │         │
        // 03 | 09:00 -> 09:30 | │         │ | │         │
        // 04 | 09:30 -> 10:00 | │         │ | │         │
        // 05 | 10:00 -> 10:30 | └─────────┘ | └─────────┘

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(2.0));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 2.5);

        Assert.isTrue(splitLessonBlocks.size() == 1,
                "The splitLessonBlocks should have 1 block");

        Assert.isTrue(splitLessonBlocks.get(0).getId() == 1,
                "The first block should have the id 1");

        Assert.isTrue(splitLessonBlocks.get(0).getBlockSize() == 2.0,
                "The first block should have the block size 2");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The first block should have the start time 08:00");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The first block should have the end time 10:30");
    }

    @Test
    public void test_1_Block_2_5_To_5_Blocks_0_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 2.5 ──┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        // 05 | 10:00 -> 10:30 | └─────────┘ | ┌── 0.5 ──┐

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(2.5));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 0.5);

        Assert.isTrue(splitLessonBlocks.size() == 5,
                "The splitLessonBlocks should have 5 blocks");

        Assert.isTrue(splitLessonBlocks.get(0).getId() == 1,
                "The first block should have the id 1");

        Assert.isTrue(splitLessonBlocks.get(1).getId() == 2,
                "The second block should have the id 2");

        Assert.isTrue(splitLessonBlocks.get(2).getId() == 3,
                "The third block should have the id 3");

        Assert.isTrue(splitLessonBlocks.get(3).getId() == 4,
                "The fourth block should have the id 4");

        Assert.isTrue(splitLessonBlocks.get(4).getId() == 5,
                "The fifth block should have the id 5");

        splitLessonBlocks.forEach(lessonBlock -> Assert.isTrue(lessonBlock.getBlockSize() == 0.5,
                "All blocks should have the block size 0.5"));

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The first block should have the start time 08:00");

        Assert.isTrue(splitLessonBlocks.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("08:30")),
                "The first block should have the end time 08:30");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("08:30")),
                "The second block should have the start time 08:30");

        Assert.isTrue(splitLessonBlocks.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("09:00")),
                "The second block should have the end time 09:00");

        Assert.isTrue(splitLessonBlocks.get(2).getTimeslot().getStartTime().equals(LocalTime.parse("09:00")),
                "The third block should have the start time 09:00");

        Assert.isTrue(splitLessonBlocks.get(2).getTimeslot().getEndTime().equals(LocalTime.parse("09:30")),
                "The third block should have the end time 09:30");

        Assert.isTrue(splitLessonBlocks.get(3).getTimeslot().getStartTime().equals(LocalTime.parse("09:30")),
                "The fourth block should have the start time 09:30");

        Assert.isTrue(splitLessonBlocks.get(3).getTimeslot().getEndTime().equals(LocalTime.parse("10:00")),
                "The fourth block should have the end time 10:00");

        Assert.isTrue(splitLessonBlocks.get(4).getTimeslot().getStartTime().equals(LocalTime.parse("10:00")),
                "The fifth block should have the start time 10:00");

        Assert.isTrue(splitLessonBlocks.get(4).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The fifth block should have the end time 10:30");
    }
}
