package ipb.pt.timetableapi.oldData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.oldData.model.Checkpoint;

@Repository
public interface CheckpointRepository extends JpaRepository<Checkpoint, Integer> {
}
