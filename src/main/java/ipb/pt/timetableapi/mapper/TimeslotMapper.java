package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.solver.SizeConstant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
public class TimeslotMapper {
    public List<Timeslot> mapTimeslotsUnitsToBlocks(List<Timeslot> timeslotBlocks, double blockSize) {
        List<Timeslot> timeslotsBlocks = new ArrayList<>();
        int timeslotUnits = (int) (blockSize / SizeConstant.SIZE_0_5);
        int duration = (int) (blockSize / SizeConstant.SIZE_0_5 * SizeConstant.UNIT_DURATION);

        for (int i = 0; i < timeslotBlocks.size(); i += timeslotUnits) {
            Timeslot timeslot = timeslotBlocks.get(i);
            Timeslot timeslotBlock = new Timeslot();
            timeslotBlock.setId(timeslot.getId());
            timeslotBlock.setDayOfWeek(timeslot.getDayOfWeek());
            timeslotBlock.setStartTime(timeslot.getStartTime());
            timeslotBlock.setEndTime(timeslot.getStartTime().plusMinutes(duration));
            timeslotsBlocks.add(timeslotBlock);
        }

        return timeslotsBlocks;
    }

    public double getTimeslotSize(double blockSize) {
        return blockSize <= SizeConstant.SIZE_0_5 ? SizeConstant.SIZE_0_5
                : blockSize <= SizeConstant.SIZE_2_5 ? SizeConstant.SIZE_2_5
                : SizeConstant.SIZE_5;
    }

    public List<Timeslot> mapUnavailabilityUnitsToBlocks(
            List<Timeslot> timeslotBlocks, List<Timeslot> unavailabilityUnits) {
        HashMap<Long, Timeslot> unavailabilityBlocksHashMap = new HashMap<>();

        for (Timeslot unavailabilityUnit : unavailabilityUnits) {
            for (Timeslot timeslotBlock : timeslotBlocks) {
                if (unavailabilityUnit.getDayOfWeek().equals(timeslotBlock.getDayOfWeek())
                        && (unavailabilityUnit.getStartTime().isAfter(timeslotBlock.getStartTime())
                        || unavailabilityUnit.getStartTime().equals(timeslotBlock.getStartTime()))
                        && (unavailabilityUnit.getEndTime().isBefore(timeslotBlock.getEndTime()))
                        || unavailabilityUnit.getEndTime().equals(timeslotBlock.getEndTime())) {
                    unavailabilityBlocksHashMap.put(timeslotBlock.getId(), timeslotBlock);
                }
            }
        }

        List<Timeslot> unavailabilityBlocks = new ArrayList<>(unavailabilityBlocksHashMap.values().stream().toList());
        unavailabilityBlocks.sort(Comparator.comparing(Timeslot::getStartTime));

        return unavailabilityBlocks;
    }
}
