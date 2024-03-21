package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.mock.LessonUnitMock;
import ipb.pt.timetableapi.model.LessonUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
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

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(4.0));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 2.5);

        Assert.isTrue(splitLessonBlocks.size() == 2,
                "The splitLessonBlocks should have 2 blocks");
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

        // it should not allow this, but let's see what we get
        Assert.isTrue(splitLessonBlocks.size() == 1,
                "The splitLessonBlocks should have 1 block");
    }

    @Test
    public void test_1_Block_2_To_1_Blocks_2_5() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 2 ───┐ | ┌── 2.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | │         │
        // 03 | 09:00 -> 09:30 | │         │ | │         │
        // 04 | 09:30 -> 10:00 | │         │ | │         │
        // 05 | 10:00 -> 10:30 | └─────────┘ | └─────────┘

        List<LessonUnit> lessonBlocks = lessonUnitMock.getLessonBlocks(List.of(2.0));
        List<LessonUnit> splitLessonBlocks = lessonUnitMapper.mapBlocksToBlocks(lessonBlocks, 2.5);

        // it should not allow this, but let's see what we get
        Assert.isTrue(splitLessonBlocks.size() == 1,
                "The splitLessonBlocks should have 1 block");
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

        // it should not allow this, but let's see what we get
        Assert.isTrue(splitLessonBlocks.size() == 5,
                "The splitLessonBlocks should have 5 blocks");
    }
}
