package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.PeriodDto;
import ipb.pt.timetableapi.model.Period;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PeriodConverter {
    public PeriodDto toDto(Period period) {
        PeriodDto periodDto = new PeriodDto();
        BeanUtils.copyProperties(period, periodDto);
        return periodDto;
    }

    public List<PeriodDto> toDto(List<Period> periods) {
        List<PeriodDto> periodDtos = new ArrayList<>();
        for (Period period : periods) {
            periodDtos.add(toDto(period));
        }

        return periodDtos;
    }

    public Period toModel(PeriodDto periodDto) {
        Period period = new Period();
        BeanUtils.copyProperties(periodDto, period);
        return period;
    }

    public List<Period> toModel(List<PeriodDto> periodDtos) {
        List<Period> periods = new ArrayList<>();
        for (PeriodDto periodDto : periodDtos) {
            periods.add(toModel(periodDto));
        }

        return periods;
    }
}
