package ipb.pt.timetableapi.oldData.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ipb.pt.timetableapi.oldData.model.Horario;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {
    List<Horario> findByIdSala(int idSala);

    @Query(value = "SELECT * FROM indisponibilidade", nativeQuery = true)
    public List<Object[]> findIndisponibilidades();
}
