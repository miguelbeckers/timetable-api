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
public class MapBlocksToUnitsTest {
    private final LessonUnitMapper lessonUnitMapper;

    @Autowired
    public MapBlocksToUnitsTest(LessonUnitMapper lessonUnitMapper) {
        this.lessonUnitMapper = lessonUnitMapper;
    }

    @Test
    public void test_1_Block_5_To_10_Units() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 5 ───┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        // 05 | 10:00 -> 10:30 | │         │ | ┌── 0.5 ──┐
        // 06 | 10:30 -> 11:00 | │         │ | ┌── 0.5 ──┐
        // 07 | 11:00 -> 11:30 | │         │ | ┌── 0.5 ──┐
        // 08 | 11:30 -> 12:00 | │         │ | ┌── 0.5 ──┐
        // 09 | 12:00 -> 12:30 | │         │ | ┌── 0.5 ──┐
        // 10 | 12:30 -> 13:00 | └─────────┘ | ┌── 0.5 ──┐

        List<LessonUnit> lessonBlocks = getBlocks(List.of(5.0));
        List<LessonUnit> lessonUnits = lessonUnitMapper.mapBlocksToUnits(lessonBlocks);

        Assert.isTrue(lessonUnits.size() == 10,
                "The number of units is not 10");

        Assert.isTrue(lessonUnits.stream().allMatch(lessonUnit -> lessonUnit.getBlockSize() == 0.5),
                "All the units do not have the block size of 0.5");

        Assert.isTrue(lessonUnits.get(0).getId() == 1,
                "The first unit does not have the id 1");

        Assert.isTrue(lessonUnits.get(1).getId() == 2,
                "The second unit does not have the id 2");

        Assert.isTrue(lessonUnits.get(2).getId() == 3,
                "The third unit does not have the id 3");

        Assert.isTrue(lessonUnits.get(3).getId() == 4,
                "The fourth unit does not have the id 4");

        Assert.isTrue(lessonUnits.get(4).getId() == 5,
                "The fifth unit does not have the id 5");

        Assert.isTrue(lessonUnits.get(5).getId() == 6,
                "The sixth unit does not have the id 6");

        Assert.isTrue(lessonUnits.get(6).getId() == 7,
                "The seventh unit does not have the id 7");

        Assert.isTrue(lessonUnits.get(7).getId() == 8,
                "The eighth unit does not have the id 8");

        Assert.isTrue(lessonUnits.get(8).getId() == 9,
                "The ninth unit does not have the id 9");

        Assert.isTrue(lessonUnits.get(9).getId() == 10,
                "The tenth unit does not have the id 10");

        Assert.isTrue(lessonUnits.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
                "The first unit does not have the start time of 08:00");

        Assert.isTrue(lessonUnits.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("08:30")),
                "The first unit does not have the end time of 08:30");

        Assert.isTrue(lessonUnits.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("08:30")),
                "The second unit does not have the start time of 08:30");

        Assert.isTrue(lessonUnits.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("09:00")),
                "The second unit does not have the end time of 09:00");

        Assert.isTrue(lessonUnits.get(2).getTimeslot().getStartTime().equals(LocalTime.parse("09:00")),
                "The third unit does not have the start time of 09:00");

        Assert.isTrue(lessonUnits.get(2).getTimeslot().getEndTime().equals(LocalTime.parse("09:30")),
                "The third unit does not have the end time of 09:30");

        Assert.isTrue(lessonUnits.get(3).getTimeslot().getStartTime().equals(LocalTime.parse("09:30")),
                "The fourth unit does not have the start time of 09:30");

        Assert.isTrue(lessonUnits.get(3).getTimeslot().getEndTime().equals(LocalTime.parse("10:00")),
                "The fourth unit does not have the end time of 10:00");

        Assert.isTrue(lessonUnits.get(4).getTimeslot().getStartTime().equals(LocalTime.parse("10:00")),
                "The fifth unit does not have the start time of 10:00");

        Assert.isTrue(lessonUnits.get(4).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
                "The fifth unit does not have the end time of 10:30");

        Assert.isTrue(lessonUnits.get(5).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
                "The sixth unit does not have the start time of 10:30");

        Assert.isTrue(lessonUnits.get(5).getTimeslot().getEndTime().equals(LocalTime.parse("11:00")),
                "The sixth unit does not have the end time of 11:00");

        Assert.isTrue(lessonUnits.get(6).getTimeslot().getStartTime().equals(LocalTime.parse("11:00")),
                "The seventh unit does not have the start time of 11:00");

        Assert.isTrue(lessonUnits.get(6).getTimeslot().getEndTime().equals(LocalTime.parse("11:30")),
                "The seventh unit does not have the end time of 11:30");

        Assert.isTrue(lessonUnits.get(7).getTimeslot().getStartTime().equals(LocalTime.parse("11:30")),
                "The eighth unit does not have the start time of 11:30");

        Assert.isTrue(lessonUnits.get(7).getTimeslot().getEndTime().equals(LocalTime.parse("12:00")),
                "The eighth unit does not have the end time of 12:00");

        Assert.isTrue(lessonUnits.get(8).getTimeslot().getStartTime().equals(LocalTime.parse("12:00")),
                "The ninth unit does not have the start time of 12:00");

        Assert.isTrue(lessonUnits.get(8).getTimeslot().getEndTime().equals(LocalTime.parse("12:30")),
                "The ninth unit does not have the end time of 12:30");

        Assert.isTrue(lessonUnits.get(9).getTimeslot().getStartTime().equals(LocalTime.parse("12:30")),
                "The tenth unit does not have the start time of 12:30");

        Assert.isTrue(lessonUnits.get(9).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
                "The tenth unit does not have the end time of 13:00");
    }

    @Test
    public void test_1_Block_4_5_To_9_Units() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 4.5 ──┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        // 05 | 10:00 -> 10:30 | │         │ | ┌── 0.5 ──┐
        // 06 | 10:30 -> 11:00 | │         │ | ┌── 0.5 ──┐
        // 07 | 11:00 -> 11:30 | │         │ | ┌── 0.5 ──┐
        // 08 | 11:30 -> 12:00 | │         │ | ┌── 0.5 ──┐
        // 09 | 12:00 -> 12:30 | │         │ | ┌── 0.5 ──┐
        //    | 12:30 -> 13:00 | └─────────┘ |

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_1_Block_4_To_8_Units() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 4 ───┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        // 05 | 10:00 -> 10:30 | │         │ | ┌── 0.5 ──┐
        // 06 | 10:30 -> 11:00 | │         │ | ┌── 0.5 ──┐
        // 07 | 11:00 -> 11:30 | │         │ | ┌── 0.5 ──┐
        // 08 | 11:30 -> 12:00 | │         │ | ┌── 0.5 ──┐
        //    | 12:00 -> 12:30 | │         │ |
        //    | 12:30 -> 13:00 | └─────────┘ |

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_1_Block_3_5_To_7_Units() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 3.5 ──┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        // 05 | 10:00 -> 10:30 | │         │ | ┌── 0.5 ──┐
        // 06 | 10:30 -> 11:00 | │         │ | ┌── 0.5 ──┐
        // 07 | 11:00 -> 11:30 | │         │ | ┌── 0.5 ──┐
        //    | 11:30 -> 12:00 | │         │ |
        //    | 12:00 -> 12:30 | │         │ |
        //    | 12:30 -> 13:00 | └─────────┘ |

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_1_Block_3_To_6_Units() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 3 ───┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        // 05 | 10:00 -> 10:30 | │         │ | ┌── 0.5 ──┐
        // 06 | 10:30 -> 11:00 | │         │ | ┌── 0.5 ──┐
        //    | 11:00 -> 11:30 | │         │ |
        //    | 11:30 -> 12:00 | │         │ |
        //    | 12:00 -> 12:30 | │         │ |
        //    | 12:30 -> 13:00 | └─────────┘ |

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_1_Block_2_5_To_5_Units() {

        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 2.5 ──┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        // 05 | 10:00 -> 10:30 | └─────────┘ │ ┌── 0.5 ──┐

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_1_Block_2_To_4_Units() {
        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 2 ───┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        //    | 10:00 -> 10:30 | └─────────┘ │

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_1_Block_1_5_To_3_Units() {
        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 1.5 ──┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        //    | 09:30 -> 10:00 | │         │ |
        //    | 10:00 -> 10:30 | └─────────┘ │

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_1_Block_1_To_2_Units() {
        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 1 ───┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | └─────────┘ | ┌── 0.5 ──┐

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_1_Block_0_5_To_1_Unit() {
        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 0.5 ──┐ | ┌── 0.5 ──┐

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_2_Blocks_2_5_To_10_Units() {
        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 2.5 ──┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        // 05 | 10:00 -> 10:30 | └─────────┘ │ ┌── 0.5 ──┐
        // 06 | 10:30 -> 11:00 | ┌── 2.5 ──┐ | ┌── 0.5 ──┐
        // 07 | 11:00 -> 11:30 | │         │ | ┌── 0.5 ──┐
        // 08 | 11:30 -> 12:00 | │         │ | ┌── 0.5 ──┐
        // 09 | 12:00 -> 12:30 | │         │ | ┌── 0.5 ──┐
        // 10 | 12:30 -> 13:00 | └─────────┘ | ┌── 0.5 ──┐

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_2_Blocks_2_To_8_Units() {
        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌─── 2 ───┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        //    | 10:00 -> 10:30 | └─────────┘ │
        // 05 | 10:30 -> 11:00 | ┌─── 2 ───┐ | ┌── 0.5 ──┐
        // 06 | 11:00 -> 11:30 | │         │ | ┌── 0.5 ──┐
        // 07 | 11:30 -> 12:00 | │         │ | ┌── 0.5 ──┐
        // 08 | 12:00 -> 12:30 | │         │ | ┌── 0.5 ──┐
        //    | 12:30 -> 13:00 | └─────────┘ |

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void test_1_Block_2_5_And_1_Block_1_5_To_8_Units() {
        // id | time           | input       | output
        // 01 | 08:00 -> 08:30 | ┌── 2.5 ──┐ | ┌── 0.5 ──┐
        // 02 | 08:30 -> 09:00 | │         │ | ┌── 0.5 ──┐
        // 03 | 09:00 -> 09:30 | │         │ | ┌── 0.5 ──┐
        // 04 | 09:30 -> 10:00 | │         │ | ┌── 0.5 ──┐
        // 05 | 10:00 -> 10:30 | └─────────┘ │ ┌── 0.5 ──┐
        // 06 | 10:30 -> 11:00 | ┌── 1.5 ──┐ | ┌── 0.5 ──┐
        // 07 | 11:00 -> 11:30 | │         │ | ┌── 0.5 ──┐
        // 08 | 11:30 -> 12:00 | │         │ | ┌── 0.5 ──┐
        //    | 12:00 -> 12:30 | │         │ |
        //    | 12:30 -> 13:00 | └─────────┘ |

        // Arrange
        // Act
        // Assert
    }

    private List<LessonUnit> getBlocks(List<Double> blockSizes) {
        List<LessonUnit> lessonBlocks = new ArrayList<>();

        Lesson lesson = new Lesson();
        lesson.setId(1L);

        Classroom classroom = new Classroom();
        classroom.setId(1L);

        LocalTime startTime = LocalTime.parse("08:00");
        LocalTime endTime = LocalTime.parse("13:00");

        long id = 1L;

        for (Double blockSize : blockSizes) {
            int unitsPerBlock = (int) (blockSize / SizeConstant.SIZE_0_5);

            Timeslot timeslot = new Timeslot();
            timeslot.setStartTime(startTime);
            timeslot.setEndTime(endTime);
            timeslot.setDayOfWeek(DayOfWeek.MONDAY);

            LessonUnit lessonBlock = new LessonUnit();
            lessonBlock.setId(id);
            lessonBlock.setLesson(lesson);
            lessonBlock.setBlockSize(blockSize);
            lessonBlock.setIsPinned(false);
            lessonBlock.setTimeslot(timeslot);
            lessonBlock.setClassroom(classroom);

            lessonBlocks.add(lessonBlock);
            id += unitsPerBlock;
        }

        return lessonBlocks;
    }
}
