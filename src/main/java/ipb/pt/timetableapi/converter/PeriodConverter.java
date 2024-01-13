package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.newData.model.Period;
import ipb.pt.timetableapi.newData.repository.PeriodRepository;
import ipb.pt.timetableapi.oldData.model.Ano;
import ipb.pt.timetableapi.oldData.repository.AnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PeriodConverter {
    @Autowired
    private AnoRepository anoRepository;

    @Autowired
    private PeriodRepository periodRepository;

    public void convert() {
        List<Ano> anos = anoRepository.findAll();
        List<Period> periods = new ArrayList<>();

        for(Ano ano : anos) {
            Period period = new Period();
            period.setId((long) ano.getId());
            period.setName(ano.getNome());

            period.setAbbreviation(ano.getAbrev());
            periods.add(period);
        }

        periodRepository.deleteAll();
        periodRepository.saveAll(periods);
    }
}
