package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
}
