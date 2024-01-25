package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.TimeslotConverter;
import ipb.pt.timetableapi.dto.TimeslotDto;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TimeslotService {
    private final TimeslotRepository timeslotRepository;
    private final TimeslotConverter timeslotConverter;

    @Autowired
    public TimeslotService(TimeslotRepository timeslotRepository, TimeslotConverter timeslotConverter) {
        this.timeslotRepository = timeslotRepository;
        this.timeslotConverter = timeslotConverter;
    }

    public List<Timeslot> findAll() {
        return timeslotRepository.findAll();
    }

    public Timeslot findById(Long id) {
        return timeslotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));
    }

    public Timeslot create(TimeslotDto timeslotDto) {
        Timeslot timeslot = timeslotConverter.toModel(timeslotDto);
        return timeslotRepository.save(timeslot);
    }

    public Timeslot update(TimeslotDto timeslotDto, Long id) {
        timeslotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));

        Timeslot timeslot = timeslotConverter.toModel(timeslotDto);
        return timeslotRepository.save(timeslot);
    }

    public void delete(Long id) {
        Timeslot timeslot = timeslotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));

        timeslotRepository.delete(timeslot);
    }

    public void deleteAll() {
        timeslotRepository.deleteAll();
    }

    public void saveAll(List<TimeslotDto> timeslotDtos) {
        List<Timeslot> timeslots = timeslotConverter.toModel(timeslotDtos);
        timeslotRepository.saveAll(timeslots);
    }
}

