package ipb.pt.timetableapi.newData.repository;

import ipb.pt.timetableapi.newData.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
