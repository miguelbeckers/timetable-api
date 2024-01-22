package ipb.pt.timetableapi.service;


import ipb.pt.timetableapi.dto.PeriodDto;
import ipb.pt.timetableapi.model.Period;
import ipb.pt.timetableapi.repository.PeriodRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PeriodService {
    @Autowired
    private PeriodRepository periodRepository;

    public List<Period> findAll() {
        return periodRepository.findAll();
    }

    public Period findById(Long id) {
        return periodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Period not found"));
    }

    public Period create(PeriodDto periodDto) {
        Period period = new Period();
        BeanUtils.copyProperties(periodDto, period);
        return periodRepository.save(period);
    }

    public Period update(PeriodDto periodDto, Long id) {
        Period period = periodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Period not found"));

        BeanUtils.copyProperties(periodDto, period);
        return periodRepository.save(period);
    }

    public void delete(Long id) {
        Period period = periodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Period not found"));

        periodRepository.delete(period);
    }

    public void createMany(List<PeriodDto> periodDtos) {
        for (PeriodDto periodDto : periodDtos) {
            Period period = new Period();
            BeanUtils.copyProperties(periodDto, period);
            periodRepository.save(period);
        }
    }
}

