package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.TimeslotConverter;
import ipb.pt.timetableapi.dto.TimeslotDto;
import ipb.pt.timetableapi.mapper.TimeslotMapper;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import ipb.pt.timetableapi.solver.SizeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TimeslotService {
    private final TimeslotRepository timeslotRepository;
    private final TimeslotConverter timeslotConverter;
    private final TimeslotMapper timeslotMapper;

    @Autowired
    public TimeslotService(TimeslotRepository timeslotRepository,
                           TimeslotConverter timeslotConverter,
                           TimeslotMapper timeslotMapper
    ) {
        this.timeslotRepository = timeslotRepository;
        this.timeslotConverter = timeslotConverter;
        this.timeslotMapper = timeslotMapper;
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

    public List<Timeslot> getTimeslotsBySize(double blockSize) {
        List<Timeslot> timeslots = timeslotRepository.findAll();
        if (blockSize == SizeConstant._0_5) return timeslots;
        return timeslotMapper.mapTimeslotsUnitsToBlocks(timeslots, blockSize);
    }

    public void updateUnavailability(List<Timeslot> timeslots, List<LessonUnit> lessonBlocks) {
        for (LessonUnit lessonBlock : lessonBlocks) {
            lessonBlock.getLesson().getProfessors().forEach(professor -> professor.setUnavailability(
                    timeslotMapper.mapUnavailabilityUnitsToBlocks(timeslots, professor.getUnavailability())));

            lessonBlock.getLesson().getSubjectCourse().getCourse().setUnavailability(timeslotMapper.mapUnavailabilityUnitsToBlocks(
                    timeslots, lessonBlock.getLesson().getSubjectCourse().getCourse().getUnavailability()));

            if (lessonBlock.getClassroom() == null) continue;
            lessonBlock.getClassroom().setUnavailability(timeslotMapper.mapUnavailabilityUnitsToBlocks(
                    timeslots, lessonBlock.getClassroom().getUnavailability()));

        }
    }
}
