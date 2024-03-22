package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.mock.TimeslotMock;
import ipb.pt.timetableapi.model.Timeslot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest
public class MapTimeslotUnitsToTimeslotBlocksTest {
    private final TimeslotMapper timeslotMapper;
    private final TimeslotMock timeslotMock;

    @Autowired
    public MapTimeslotUnitsToTimeslotBlocksTest(TimeslotMapper timeslotMapper, TimeslotMock timeslotMock) {
        this.timeslotMapper = timeslotMapper;
        this.timeslotMock = timeslotMock;
    }

    @Test
    public void test_Timeslots_Units_To_Timeslot_Of_Size_5() {
        // id | input                | output
        // 01 | MON - 08:00 -> 08:30 | MON - 08:00 ┐
        // 02 | MON - 08:30 -> 09:00 |             │
        // 03 | MON - 09:00 -> 09:30 |             │
        // 04 | MON - 09:30 -> 10:00 |             │
        // 05 | MON - 10:00 -> 10:30 |             │
        // 06 | MON - 10:30 -> 11:00 |             │
        // 07 | MON - 11:00 -> 11:30 |             │
        // 08 | MON - 11:30 -> 12:00 |             │
        // 09 | MON - 12:00 -> 12:30 |             │
        // 10 | MON - 12:30 -> 13:00 |             └> 13:00
        // 11 | MON - 13:00 -> 13:30 | MON - 13:00 ┐
        // 12 | MON - 13:30 -> 14:00 |             │
        // 13 | MON - 14:00 -> 14:30 |             │
        // 14 | MON - 14:30 -> 15:00 |             │
        // 15 | MON - 15:00 -> 15:30 |             │
        // 16 | MON - 15:30 -> 16:00 |             │
        // 17 | MON - 16:00 -> 16:30 |             │
        // 18 | MON - 16:30 -> 17:00 |             │
        // 19 | MON - 17:00 -> 17:30 |             │
        // 20 | MON - 17:30 -> 18:00 |             └> 18:00
        // 21 | MON - 18:00 -> 18:30 | MON - 18:00 ┐
        // 22 | MON - 18:30 -> 19:00 |             │
        // 23 | MON - 19:00 -> 19:30 |             │
        // 24 | MON - 19:30 -> 20:00 |             │
        // 25 | MON - 20:00 -> 20:30 |             │
        // 26 | MON - 20:30 -> 21:00 |             │
        // 27 | MON - 21:00 -> 21:30 |             │
        // 28 | MON - 21:30 -> 22:00 |             │
        // 29 | MON - 22:00 -> 22:30 |             │
        // 30 | MON - 22:30 -> 23:00 |             └> 23:00

        List<Timeslot> timeslotUnits = timeslotMock.getTimeslotUnits(15, 1L, "08:00");
        List<Timeslot> timeslotBlocks = timeslotMapper.mapTimeslotsUnitsToTimeslotsBlocks(timeslotUnits, 5);

        Assert.isTrue(timeslotBlocks.size() == 3,
                "The list should have 3 elements");

        Assert.isTrue(timeslotBlocks.get(0).getId() == 1,
                "The first element should have the id 1");

        Assert.isTrue(timeslotBlocks.get(1).getId() == 11,
                "The second element should have the id 11");

        Assert.isTrue(timeslotBlocks.get(2).getId() == 21,
                "The third element should have the id 21");

        Assert.isTrue(timeslotBlocks.get(0).getStartTime().equals(LocalTime.parse("08:00")),
                "The first element should have the start time 08:00");

        Assert.isTrue(timeslotBlocks.get(0).getEndTime().equals(LocalTime.parse("13:00")),
                "The first element should have the end time 13:00");

        Assert.isTrue(timeslotBlocks.get(1).getStartTime().equals(LocalTime.parse("13:00")),
                "The second element should have the start time 13:00");

        Assert.isTrue(timeslotBlocks.get(1).getEndTime().equals(LocalTime.parse("18:00")),
                "The second element should have the end time 18:00");

        Assert.isTrue(timeslotBlocks.get(2).getStartTime().equals(LocalTime.parse("18:00")),
                "The third element should have the start time 18:00");

        Assert.isTrue(timeslotBlocks.get(2).getEndTime().equals(LocalTime.parse("23:00")),
                "The third element should have the end time 23:00");
    }

    @Test
    public void test_Timeslots_Units_To_Timeslot_Of_Size_2_5() {
        // id | input                | output
        // 01 | MON - 08:00 -> 08:30 |  MON - 08:00 ┐
        // 02 | MON - 08:30 -> 09:00 |              │
        // 03 | MON - 09:00 -> 09:30 |              │
        // 04 | MON - 09:30 -> 10:00 |              │
        // 05 | MON - 10:00 -> 10:30 |              └> 10:30
        // 06 | MON - 10:30 -> 11:00 |  MON - 10:30 ┐
        // 07 | MON - 11:00 -> 11:30 |              │
        // 08 | MON - 11:30 -> 12:00 |              │
        // 09 | MON - 12:00 -> 12:30 |              │
        // 10 | MON - 12:30 -> 13:00 |              └> 13:00
        // 11 | MON - 13:00 -> 13:30 |  MON - 13:00 ┐
        // 12 | MON - 13:30 -> 14:00 |              │
        // 13 | MON - 14:00 -> 14:30 |              │
        // 14 | MON - 14:30 -> 15:00 |              │
        // 15 | MON - 15:00 -> 15:30 |              └> 15:30
        // 16 | MON - 15:30 -> 16:00 |  MON - 15:30 ┐
        // 17 | MON - 16:00 -> 16:30 |              │
        // 18 | MON - 16:30 -> 17:00 |              │
        // 19 | MON - 17:00 -> 17:30 |              │
        // 20 | MON - 17:30 -> 18:00 |              └> 18:00
        // 21 | MON - 18:00 -> 18:30 |  MON - 18:00 ┐
        // 22 | MON - 18:30 -> 19:00 |              │
        // 23 | MON - 19:00 -> 19:30 |              │
        // 24 | MON - 19:30 -> 20:00 |              │
        // 25 | MON - 20:00 -> 20:30 |              └> 20:30
        // 26 | MON - 20:30 -> 21:00 |  MON - 20:30 ┐
        // 27 | MON - 21:00 -> 21:30 |              │
        // 28 | MON - 21:30 -> 22:00 |              │
        // 29 | MON - 22:00 -> 22:30 |              │
        // 30 | MON - 22:30 -> 23:00 |              └> 23:00

        List<Timeslot> timeslotUnits = timeslotMock.getTimeslotUnits(15, 1L, "08:00");
        List<Timeslot> timeslotBlocks = timeslotMapper.mapTimeslotsUnitsToTimeslotsBlocks(timeslotUnits, 2.5);

        Assert.isTrue(timeslotBlocks.size() == 6,
                "The list should have 6 elements");

        Assert.isTrue(timeslotBlocks.get(0).getId() == 1,
                "The first element should have the id 1");

        Assert.isTrue(timeslotBlocks.get(1).getId() == 6,
                "The second element should have the id 6");

        Assert.isTrue(timeslotBlocks.get(2).getId() == 11,
                "The third element should have the id 11");

        Assert.isTrue(timeslotBlocks.get(3).getId() == 16,
                "The fourth element should have the id 16");

        Assert.isTrue(timeslotBlocks.get(4).getId() == 21,
                "The fifth element should have the id 21");

        Assert.isTrue(timeslotBlocks.get(5).getId() == 26,
                "The sixth element should have the id 26");

        Assert.isTrue(timeslotBlocks.get(0).getStartTime().equals(LocalTime.parse("08:00")),
                "The first element should have the start time 08:00");

        Assert.isTrue(timeslotBlocks.get(0).getEndTime().equals(LocalTime.parse("10:30")),
                "The first element should have the end time 10:30");

        Assert.isTrue(timeslotBlocks.get(1).getStartTime().equals(LocalTime.parse("10:30")),
                "The second element should have the start time 10:30");

        Assert.isTrue(timeslotBlocks.get(1).getEndTime().equals(LocalTime.parse("13:00")),
                "The second element should have the end time 13:00");

        Assert.isTrue(timeslotBlocks.get(2).getStartTime().equals(LocalTime.parse("13:00")),
                "The third element should have the start time 13:00");

        Assert.isTrue(timeslotBlocks.get(2).getEndTime().equals(LocalTime.parse("15:30")),
                "The third element should have the end time 15:30");

        Assert.isTrue(timeslotBlocks.get(3).getStartTime().equals(LocalTime.parse("15:30")),
                "The fourth element should have the start time 15:30");

        Assert.isTrue(timeslotBlocks.get(3).getEndTime().equals(LocalTime.parse("18:00")),
                "The fourth element should have the end time 18:00");

        Assert.isTrue(timeslotBlocks.get(4).getStartTime().equals(LocalTime.parse("18:00")),
                "The fifth element should have the start time 18:00");

        Assert.isTrue(timeslotBlocks.get(4).getEndTime().equals(LocalTime.parse("20:30")),
                "The fifth element should have the end time 20:30");

        Assert.isTrue(timeslotBlocks.get(5).getStartTime().equals(LocalTime.parse("20:30")),
                "The sixth element should have the start time 20:30");

        Assert.isTrue(timeslotBlocks.get(5).getEndTime().equals(LocalTime.parse("23:00")),
                "The sixth element should have the end time 23:00");
    }

    @Test
    public void testUpdateTimeslotAccordingTo_Size_5() {
        // id | input                | output
        // 01 | MON - 08:00 -> 08:30 | MON - 08:00 ┐
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             └> 13:00

        List<Timeslot> timeslotUnits = timeslotMock.getTimeslotUnits(0.5, 1L, "08:00");
        List<Timeslot> timeslotBlocks = timeslotMapper.mapTimeslotsUnitsToTimeslotsBlocks(timeslotUnits, 5);

        Assert.isTrue(timeslotBlocks.size() == 1,
                "The list should have 1 element");

        Assert.isTrue(timeslotBlocks.get(0).getId() == 1,
                "The first element should have the id 1");

        Assert.isTrue(timeslotBlocks.get(0).getStartTime().equals(LocalTime.parse("08:00")),
                "The first element should have the start time 08:00");

        Assert.isTrue(timeslotBlocks.get(0).getEndTime().equals(LocalTime.parse("13:00")),
                "The first element should have the end time 13:00");
    }

    @Test
    public void testUpdateTimeslotAccordingTo_Size_2_5() {
        // id | input                | output
        // 01 | MON - 08:00 -> 08:30 | MON - 08:00 ┐
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             └> 10:30

        List<Timeslot> timeslotUnits = timeslotMock.getTimeslotUnits(0.5, 1L, "08:00");
        List<Timeslot> timeslotBlocks = timeslotMapper.mapTimeslotsUnitsToTimeslotsBlocks(timeslotUnits, 2.5);

        Assert.isTrue(timeslotBlocks.size() == 1,
                "The list should have 1 element");

        Assert.isTrue(timeslotBlocks.get(0).getId() == 1,
                "The first element should have the id 1");

        Assert.isTrue(timeslotBlocks.get(0).getStartTime().equals(LocalTime.parse("08:00")),
                "The first element should have the start time 08:00");

        Assert.isTrue(timeslotBlocks.get(0).getEndTime().equals(LocalTime.parse("10:30")),
                "The first element should have the end time 10:30");
    }

    @Test
    public void testUpdateTimeslotCheckingSnap() {
        // id | input                | output
        //    |                      | MON - 08:00 ┐
        // 02 | MON - 08:30 -> 09:00 |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             └> 13:00

        // Arrange
        // Act
        // Assert
    }
}
