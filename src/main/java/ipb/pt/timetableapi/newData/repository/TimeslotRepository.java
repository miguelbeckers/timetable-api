package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
}
