package ipb.pt.timetableapi.mapper.timeslotMapper;

import ipb.pt.timetableapi.mapper.TimeslotMapper;
import ipb.pt.timetableapi.mock.TimeslotMock;
import ipb.pt.timetableapi.model.Timeslot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MapUnavailabilityUnitsToBlocksTest {
    private final TimeslotMapper timeslotMapper;
    private final TimeslotMock timeslotMock;

    @Autowired
    public MapUnavailabilityUnitsToBlocksTest(TimeslotMapper timeslotMapper, TimeslotMock timeslotMock) {
        this.timeslotMapper = timeslotMapper;
        this.timeslotMock = timeslotMock;
    }

    @Test
    public void testMapUnavailabilityUnitsToBlocks_empty() {

        // id | input                | output
        // 01 |                      |  MON - 08:00 ┐
        // 02 |                      |              │
        // 03 |                      |              │
        // 04 |                      |              │
        // 05 |                      |              └> 10:30

        List<Timeslot> timeslotUnits = timeslotMock.getTimeslotUnits(15, 1L, "08:00");
        List<Timeslot> timeslotBlocks = timeslotMapper.mapTimeslotsUnitsToBlocks(timeslotUnits, 2.5);

        List<Timeslot> unavailability = new ArrayList<>();
        List<Timeslot> unavailabilityBlocks = timeslotMapper.mapUnavailabilityUnitsToBlocks(timeslotBlocks, unavailability);

        Assert.isTrue(unavailabilityBlocks.isEmpty(),
                "The number of elements of unavailableBlocks is not empty");
    }

    @Test
    public void testMapUnavailabilityUnitsToBlocks_beginning() {

        // id | input                | output
        // 01 | MON - 08:00 -> 08:30 |  MON - 08:00 ┐
        // 02 |                      |              │
        // 03 |                      |              │
        // 04 |                      |              │
        // 05 |                      |              └> 10:30

        List<Timeslot> timeslotUnits = timeslotMock.getTimeslotUnits(15, 1L, "08:00");
        List<Timeslot> timeslotBlocks = timeslotMapper.mapTimeslotsUnitsToBlocks(timeslotUnits, 2.5);

        List<Timeslot> unavailability = new ArrayList<>();
        unavailability.add(timeslotUnits.get(0));
        List<Timeslot> unavailabilityBlocks = timeslotMapper.mapUnavailabilityUnitsToBlocks(timeslotBlocks, unavailability);

        Assert.isTrue(unavailabilityBlocks.size() == 1,
                "The number of elements of unavailableBlocks is not equal to 1");

        Assert.isTrue(unavailabilityBlocks.get(0).getStartTime().toString().equals("08:00"),
                "The start time of the first element of unavailabilityBlocks is not equal to 08:00");
    }

    @Test
    public void testMapUnavailabilityUnitsToBlocks_end() {

        // id | input                | output
        // 01 |                      |  MON - 08:00 ┐
        // 02 |                      |              │
        // 03 |                      |              │
        // 04 |                      |              │
        // 05 | MON - 10:00 -> 10:30 |              └> 10:30

        List<Timeslot> timeslotUnits = timeslotMock.getTimeslotUnits(15, 1L, "08:00");
        List<Timeslot> timeslotBlocks = timeslotMapper.mapTimeslotsUnitsToBlocks(timeslotUnits, 2.5);

        List<Timeslot> unavailability = new ArrayList<>();
        unavailability.add(timeslotUnits.get(4));
        List<Timeslot> unavailabilityBlocks = timeslotMapper.mapUnavailabilityUnitsToBlocks(timeslotBlocks, unavailability);

        Assert.isTrue(unavailabilityBlocks.size() == 1,
                "The number of elements of unavailableBlocks is not equal to 1");

        Assert.isTrue(unavailabilityBlocks.get(0).getStartTime().toString().equals("08:00"),
                "The start time of the first element of unavailabilityBlocks is not equal to 08:00");
    }

    @Test
    public void testMapUnavailabilityUnitsToBlocks_middle() {

        // id | input                | output
        // 01 |                      |  MON - 08:00 ┐
        // 02 |                      |              │
        // 03 | MON - 09:00 -> 09:30 |              │
        // 04 |                      |              │
        // 05 |                      |              └> 10:30

        List<Timeslot> timeslotUnits = timeslotMock.getTimeslotUnits(15, 1L, "08:00");
        List<Timeslot> timeslotBlocks = timeslotMapper.mapTimeslotsUnitsToBlocks(timeslotUnits, 2.5);

        List<Timeslot> unavailability = new ArrayList<>();
        unavailability.add(timeslotUnits.get(2));
        List<Timeslot> unavailabilityBlocks = timeslotMapper.mapUnavailabilityUnitsToBlocks(timeslotBlocks, unavailability);

        Assert.isTrue(unavailabilityBlocks.size() == 1,
                "The number of elements of unavailableBlocks is not equal to 1");

        Assert.isTrue(unavailabilityBlocks.get(0).getStartTime().toString().equals("08:00"),
                "The start time of the first element of unavailabilityBlocks is not equal to 08:00");
    }

    @Test
    public void testMapUnavailabilityUnitsToBlocks_multiple() {

        // id | input                | output
        // 01 |                      |  MON - 08:00 ┐
        // 02 |                      |              │
        // 03 |                      |              │
        // 04 |                      |              │
        // 05 |                      |              └> 10:30
        // 06 | MON - 10:30 -> 11:00 |  MON - 10:30 ┐
        // 07 | MON - 11:00 -> 11:30 |              │
        // 08 | MON - 11:30 -> 12:00 |              │ok
        // 09 | MON - 12:00 -> 12:30 |              │
        // 10 | MON - 12:30 -> 13:00 |              └> 13:00
        // 11 | MON - 13:00 -> 13:30 |  MON - 13:00 ┐
        // 12 |                      |              │
        // 13 |                      |              │ok
        // 14 |                      |              │
        // 15 |                      |              └> 15:30
        // 16 | MON - 15:30 -> 16:00 |  MON - 15:30 ┐
        // 17 | MON - 16:00 -> 16:30 |              │
        // 18 | MON - 16:30 -> 17:00 |              │ok
        // 19 | MON - 17:00 -> 17:30 |              │
        // 20 |                      |              └> 18:00
        // 21 | MON - 18:00 -> 18:30 |  MON - 18:00 ┐
        // 22 |                      |              │
        // 23 |                      |              │ok
        // 24 |                      |              │
        // 25 |                      |              └> 20:30
        // 26 |                      |  MON - 20:30 ┐
        // 27 |                      |              │
        // 28 |                      |              │
        // 29 |                      |              │
        // 30 | MON - 22:30 -> 23:00 |              └> 23:00

        List<Timeslot> timeslotUnits = timeslotMock.getTimeslotUnits(15, 1L, "08:00");
        List<Timeslot> timeslotBlocks = timeslotMapper.mapTimeslotsUnitsToBlocks(timeslotUnits, 2.5);

        List<Timeslot> unavailability = new ArrayList<>();
        unavailability.add(timeslotUnits.get(5));
        unavailability.add(timeslotUnits.get(6));
        unavailability.add(timeslotUnits.get(7));
        unavailability.add(timeslotUnits.get(8));
        unavailability.add(timeslotUnits.get(9));
        unavailability.add(timeslotUnits.get(10));
        unavailability.add(timeslotUnits.get(15));
        unavailability.add(timeslotUnits.get(16));
        unavailability.add(timeslotUnits.get(17));
        unavailability.add(timeslotUnits.get(18));
        unavailability.add(timeslotUnits.get(20));
        unavailability.add(timeslotUnits.get(29));

        List<Timeslot> unavailabilityBlocks = timeslotMapper.mapUnavailabilityUnitsToBlocks(timeslotBlocks, unavailability);

        Assert.isTrue(unavailabilityBlocks.size() == 5,
                "The number of elements of unavailableBlocks is not equal to 5");

        Assert.isTrue(unavailabilityBlocks.get(0).getId() == 6,
                "The id of the first element of unavailabilityBlocks is not equal to 6");
    }
}
