package ipb.pt.timetableapi.service;


import ipb.pt.timetableapi.converter.PeriodConverter;
import ipb.pt.timetableapi.dto.PeriodDto;
import ipb.pt.timetableapi.model.Period;
import ipb.pt.timetableapi.repository.PeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PeriodService {
    private final PeriodRepository periodRepository;
    private final PeriodConverter periodConverter;

    @Autowired
    public PeriodService(PeriodRepository periodRepository, PeriodConverter periodConverter) {
        this.periodRepository = periodRepository;
        this.periodConverter = periodConverter;
    }

    public List<Period> findAll() {
        return periodRepository.findAll();
    }

    public Period findById(Long id) {
        return periodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Period not found"));
    }

    public Period create(PeriodDto periodDto) {
        Period period = periodConverter.toModel(periodDto);
        return periodRepository.save(period);
    }

    public Period update(PeriodDto periodDto, Long id) {
        periodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Period not found"));

        Period period = periodConverter.toModel(periodDto);
        return periodRepository.save(period);
    }

    public void delete(Long id) {
        Period period = periodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Period not found"));

        periodRepository.delete(period);
    }

    public void deleteAll() {
        periodRepository.deleteAll();
    }

    public void saveAll(List<PeriodDto> periodDtos) {
        List<Period> periods = periodConverter.toModel(periodDtos);
        periodRepository.saveAll(periods);
    }
}

