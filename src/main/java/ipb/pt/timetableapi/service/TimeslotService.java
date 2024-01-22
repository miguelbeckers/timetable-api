package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.TimeslotDto;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TimeslotService {
    @Autowired
    private TimeslotRepository timeslotRepository;

    public List<Timeslot> findAll() {
        return timeslotRepository.findAll();
    }

    public Timeslot findById(Long id) {
        return timeslotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));
    }

    public Timeslot create(TimeslotDto timeslotDto) {
        Timeslot timeslot = new Timeslot();
        BeanUtils.copyProperties(timeslotDto, timeslot);
        return timeslotRepository.save(timeslot);
    }

    public Timeslot update(TimeslotDto timeslotDto, Long id) {
        Timeslot timeslot = timeslotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));

        BeanUtils.copyProperties(timeslotDto, timeslot);
        return timeslotRepository.save(timeslot);
    }

    public void delete(Long id) {
        Timeslot timeslot = timeslotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));

        timeslotRepository.delete(timeslot);
    }

    public void createMany(List<TimeslotDto> timeslotDtos) {
        for (TimeslotDto timeslotDto : timeslotDtos) {
            Timeslot timeslot = new Timeslot();
            BeanUtils.copyProperties(timeslotDto, timeslot);
            timeslotRepository.save(timeslot);
        }
    }
}

