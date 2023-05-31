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
    private TimeslotRepository timeslotRepository;

    public List<Timeslot> findAll(){
        return timeslotRepository.findAll();
    }

    public Optional<Timeslot> findById(Long id){
        return timeslotRepository.findById(id);
    }

    public Timeslot create(Timeslot timeslot){
        return timeslotRepository.save(timeslot);
    }

    public Timeslot update(Timeslot timeslot){
        return timeslotRepository.save(timeslot);
    }

    public void delete(Timeslot timeslot){
        timeslotRepository.delete(timeslot);
    }
}
