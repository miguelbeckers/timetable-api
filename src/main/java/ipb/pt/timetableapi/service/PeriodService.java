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

    public List<PeriodDto> findAll() {
        return periodConverter.toDto(periodRepository.findAll());
    }

    public PeriodDto findById(Long id) {
        return periodConverter.toDto(periodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Period not found")));
    }

    public PeriodDto create(PeriodDto periodDto) {
        Period period = periodConverter.toModel(periodDto);
        return periodConverter.toDto(periodRepository.save(period));
    }

    public PeriodDto update(PeriodDto periodDto, Long id) {
        periodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Period not found"));

        Period period = periodConverter.toModel(periodDto);
        return periodConverter.toDto(periodRepository.save(period));
    }

    public void delete(Long id) {
        Period period = periodRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Period not found"));

        periodRepository.delete(period);
    }

    public void deleteAll() {
        periodRepository.deleteAll();
    }

    public List<PeriodDto> saveAll(List<PeriodDto> periodDtos) {
        List<Period> periods = periodConverter.toModel(periodDtos);
        return periodConverter.toDto(periodRepository.saveAll(periods));
    }
}

