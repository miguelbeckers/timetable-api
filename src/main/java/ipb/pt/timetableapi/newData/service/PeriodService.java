package ipb.pt.timetableapi.newData.service;

import ipb.pt.timetableapi.converter.PeriodConverter;
import ipb.pt.timetableapi.newData.model.Period;
import ipb.pt.timetableapi.newData.repository.PeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeriodService {
    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private PeriodConverter periodConverter;

    public List<Period> findAll(){
        return periodRepository.findAll();
    }

    public Optional<Period> findById(Long id){
        return periodRepository.findById(id);
    }

    public Period create(Period period){
        return periodRepository.save(period);
    }

    public Period update(Period period){
        return periodRepository.save(period);
    }

    public void delete(Period period){
        periodRepository.delete(period);
    }

    public void convert() {
        periodConverter.convert();
    }
}
