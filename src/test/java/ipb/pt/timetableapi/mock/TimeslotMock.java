package ipb.pt.timetableapi.mock;

import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.solver.SizeConstant;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeslotMock {

    public List<Timeslot> getTimeslotUnits(double numberOfHOurs, long initialId, String initialTime) {
        List<Timeslot> timeslotUnits = new ArrayList<>();

        LocalTime startTime = LocalTime.parse(initialTime);
        int numberOfUnits = (int) (numberOfHOurs / SizeConstant.SIZE_0_5);

        for (long i = initialId; i <= numberOfUnits; i++) {
            LocalTime endTime = startTime.plusMinutes(SizeConstant.UNIT_DURATION);

            Timeslot timeslotUnit = new Timeslot();
            timeslotUnit.setId(i);
            timeslotUnit.setStartTime(startTime);
            timeslotUnit.setEndTime(endTime);
            timeslotUnit.setDayOfWeek(DayOfWeek.MONDAY);
            timeslotUnits.add(timeslotUnit);

            startTime = endTime;
        }

        return timeslotUnits;
    }
}
