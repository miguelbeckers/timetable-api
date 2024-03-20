package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.solver.SizeConstant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimeslotMapper {
    public List<Timeslot> mapTimeslotsOfUnitsToTimeslotsOfBlocks(List<Timeslot> timeslots, double blockSize) {
        if (timeslots.size() % blockSize != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The timeslots size is not multiple of " + blockSize);
        }

        List<Timeslot> newTimeslots = new ArrayList<>();

        while (!timeslots.isEmpty()) {
            Timeslot timeslot = timeslots.get(0);
            int remainingUnits = (int) (blockSize / SizeConstant.SIZE_0_5);

            Timeslot newTimeslot = new Timeslot();
            newTimeslot.setId(timeslot.getId());
            newTimeslot.setDayOfWeek(timeslot.getDayOfWeek());
            newTimeslot.setStartTime(timeslot.getStartTime());

            while (remainingUnits > 0) {
                timeslot = timeslots.remove(0);
                remainingUnits--;
            }

            newTimeslot.setEndTime(timeslot.getEndTime());
            newTimeslots.add(newTimeslot);
        }

        return newTimeslots;
    }
}
