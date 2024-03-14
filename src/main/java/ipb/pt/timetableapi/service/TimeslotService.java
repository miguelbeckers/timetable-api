package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.TimeslotConverter;
import ipb.pt.timetableapi.dto.TimeslotDto;
import ipb.pt.timetableapi.constant.TimeslotConstant;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.ArrayList;
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

    public List<TimeslotDto> findAll() {
        return timeslotConverter.toDto(timeslotRepository.findAll());
    }

    public TimeslotDto findById(Long id) {
        return timeslotConverter.toDto(timeslotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found")));
    }

    public TimeslotDto create(TimeslotDto timeslotDto) {
        Timeslot timeslot = timeslotConverter.toModel(timeslotDto);
        return timeslotConverter.toDto(timeslotRepository.save(timeslot));
    }

    public TimeslotDto update(TimeslotDto timeslotDto, Long id) {
        timeslotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));

        Timeslot timeslot = timeslotConverter.toModel(timeslotDto);
        return timeslotConverter.toDto(timeslotRepository.save(timeslot));
    }

    public void delete(Long id) {
        Timeslot timeslot = timeslotRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Timeslot not found"));

        timeslotRepository.delete(timeslot);
    }

    public void deleteAll() {
        timeslotRepository.deleteAll();
    }

    public List<TimeslotDto> saveAll(List<TimeslotDto> timeslotDtos) {
        List<Timeslot> timeslots = timeslotConverter.toModel(timeslotDtos);
        return timeslotConverter.toDto(timeslotRepository.saveAll(timeslots));
    }

    public List<Timeslot> getTimeslots(double blockSize) {
        return getTimeslots(timeslotRepository.findAll(), blockSize);
    }

    public List<Timeslot> getTimeslots(List<Timeslot> timeslots, double blockSize) {
        if(timeslots.size() % blockSize != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The timeslots size is not multiple of " + blockSize);
        }

        List<Timeslot> newTimeslots = new ArrayList<>();

        while (!timeslots.isEmpty()) {
            Timeslot timeslot = timeslots.get(0);
            int remainingUnits = (int) (blockSize / TimeslotConstant.SIZE_0_5);

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

    public List<Timeslot> splitTimeslot(Timeslot timeslot, double blockSize) {
        List<Timeslot> splitTimeslots = new ArrayList<>();

        Duration duration = Duration.between(timeslot.getStartTime(), timeslot.getEndTime());
        double halfDurationDouble = (double) duration.toHours() / 2;
        int units = (int) (halfDurationDouble / TimeslotConstant.SIZE_0_5);

        Timeslot firstTimeslot = new Timeslot();
        firstTimeslot.setId(timeslot.getId());
        firstTimeslot.setDayOfWeek(timeslot.getDayOfWeek());
        firstTimeslot.setStartTime(timeslot.getStartTime());
        firstTimeslot.setEndTime(timeslot.getStartTime().plus(duration.dividedBy(2)));
        splitTimeslots.add(firstTimeslot);

        Timeslot secondTimeslot = new Timeslot();
        secondTimeslot.setId(timeslot.getId() + units);
        secondTimeslot.setDayOfWeek(timeslot.getDayOfWeek());
        secondTimeslot.setStartTime(timeslot.getStartTime().plus(duration.dividedBy(2)));
        secondTimeslot.setEndTime(timeslot.getEndTime());
        splitTimeslots.add(secondTimeslot);

        return splitTimeslots;
    }
}
