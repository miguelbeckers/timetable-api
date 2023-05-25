package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.model.Timeslot;
import ipb.pt.timetableapi.repository.TimeslotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeslotService {

    @Autowired
    private TimeslotRepository timeSlotRepository;

    public List<Timeslot> findAll(){
        return timeSlotRepository.findAll();
    }

    public Optional<Timeslot> findById(Long id){
        return timeSlotRepository.findById(id);
    }

    public Timeslot create(Timeslot timeSlot){
        return timeSlotRepository.save(timeSlot);
    }

    public Timeslot update(Timeslot timeSlot){
        return timeSlotRepository.save(timeSlot);
    }

    public void delete(Timeslot timeSlot){
        timeSlotRepository.delete(timeSlot);
    }
}
