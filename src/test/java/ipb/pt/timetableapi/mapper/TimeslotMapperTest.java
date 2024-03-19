package ipb.pt.timetableapi.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TimeslotMapperTest {
    private final TimeslotMapper timeslotMapper;

    @Autowired
    public TimeslotMapperTest(TimeslotMapper timeslotMapper) {
        this.timeslotMapper = timeslotMapper;
    }

    @Test
    public void testMapTimeslotsOfUnitsToTimeslotsOfBlocks() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void testMapTimeslotOfAUnitToTimeslotOfABlock() {
        // Arrange
        // Act
        // Assert
    }
}
