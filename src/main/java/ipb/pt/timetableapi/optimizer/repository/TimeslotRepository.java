package ipb.pt.timetableapi.optimizer.repository;

import ipb.pt.timetableapi.optimizer.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
}
