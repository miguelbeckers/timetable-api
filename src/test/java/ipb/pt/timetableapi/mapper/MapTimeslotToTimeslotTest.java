package ipb.pt.timetableapi.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapTimeslotToTimeslotTest {
    private final TimeslotMapper timeslotMapper;

    @Autowired
    public MapTimeslotToTimeslotTest(TimeslotMapper timeslotMapper) {
        this.timeslotMapper = timeslotMapper;
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

        // Arrange
        // Act
        // Assert
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

        // Arrange
        // Act
        // Assert
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

        // Arrange
        // Act
        // Assert
    }

    @Test
    public void testUpdateTimeslotAccordingTo_Size_2_5() {
        // id | input                | output
        // 01 | MON - 08:00 -> 08:30 | MON - 08:00 ┐
        //    |                      |             │
        //    |                      |             │
        //    |                      |             │
        //    |                      |             └> 10:30

        // Arrange
        // Act
        // Assert
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
