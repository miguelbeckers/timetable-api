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

    public List<Timeslot> mapTimeslotsOfUnitsToTimeslotsOfBlocks2(List<Timeslot> timeslots, double blockSize) {
        List<Timeslot> newTimeslots = new ArrayList<>();
        int timeslotUnits = (int) (blockSize / SizeConstant.SIZE_0_5);

        for (int i = 0; i < timeslots.size(); i += timeslotUnits) {
            Timeslot startTimeSlot = timeslots.get(i);
            Timeslot endTimeSlot = timeslots.get(Math.min(i + timeslotUnits - 1, timeslots.size() - 1));

            Timeslot newTimeslot = new Timeslot();
            newTimeslot.setId(startTimeSlot.getId());
            newTimeslot.setDayOfWeek(startTimeSlot.getDayOfWeek());
            newTimeslot.setStartTime(startTimeSlot.getStartTime());
            newTimeslot.setEndTime(endTimeSlot.getEndTime());

            newTimeslots.add(newTimeslot);
        }

        return newTimeslots;
    }

    public double getTimeslotSize(double blockSize) {
        return blockSize <= SizeConstant.SIZE_0_5 ? SizeConstant.SIZE_0_5
                : blockSize <= SizeConstant.SIZE_2_5 ? SizeConstant.SIZE_2_5
                : SizeConstant.SIZE_5;
    }
}
