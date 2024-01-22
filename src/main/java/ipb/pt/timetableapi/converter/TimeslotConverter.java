package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.TimeslotDto;
import ipb.pt.timetableapi.model.Timeslot;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TimeslotConverter {
    public TimeslotDto toDto(Timeslot timeslot) {
        TimeslotDto timeslotDto = new TimeslotDto();
        BeanUtils.copyProperties(timeslot, timeslotDto);
        return timeslotDto;
    }

    public List<TimeslotDto> toDto(List<Timeslot> timeslots) {
        List<TimeslotDto> timeslotDtos = new ArrayList<>();
        for (Timeslot timeslot : timeslots) {
            timeslotDtos.add(toDto(timeslot));
        }

        return timeslotDtos;
    }

    public Timeslot toModel(TimeslotDto timeslotDto) {
        Timeslot timeslot = new Timeslot();
        BeanUtils.copyProperties(timeslotDto, timeslot);
        return timeslot;
    }

    public List<Timeslot> toModel(List<TimeslotDto> timeslotDtos) {
        List<Timeslot> timeslots = new ArrayList<>();
        for (TimeslotDto timeslotDto : timeslotDtos) {
            timeslots.add(toModel(timeslotDto));
        }

        return timeslots;
    }
}
