package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.solver.SizeConstant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimeslotMapper {
    public List<Timeslot> mapTimeslotsUnitsToTimeslotsBlocks(List<Timeslot> timeslots, double blockSize) {
        List<Timeslot> newTimeslots = new ArrayList<>();
        int timeslotUnits = (int) (blockSize / SizeConstant.SIZE_0_5);
        int duration = (int) (blockSize / SizeConstant.SIZE_0_5 * SizeConstant.UNIT_DURATION);

        for (int i = 0; i < timeslots.size(); i += timeslotUnits) {
            Timeslot timeslot = timeslots.get(i);
            Timeslot newTimeslot = new Timeslot();
            newTimeslot.setId(timeslot.getId());
            newTimeslot.setDayOfWeek(timeslot.getDayOfWeek());
            newTimeslot.setStartTime(timeslot.getStartTime());
            newTimeslot.setEndTime(timeslot.getStartTime().plusMinutes(duration));

            newTimeslots.add(newTimeslot);
        }

        return newTimeslots;
    }

    public double getTimeslotSize(double blockSize) {
        return blockSize <= SizeConstant.SIZE_0_5 ? SizeConstant.SIZE_0_5
                : blockSize <= SizeConstant.SIZE_2_5 ? SizeConstant.SIZE_2_5
                : SizeConstant.SIZE_5;
    }

    // Quero receber uma lista de indisponibilidades e mapear elas
    // para isso é preciso dar snap.
    // Por exemplo, essa indisponibilidade poderia ser assim:

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

    // nesse caso, não há problemas, pode se usar a conversão normal de unidade para bloco
    // no entanto, se começar no meio de um bloco, ainda assim os blocos deveriam ser os mesmos
}
