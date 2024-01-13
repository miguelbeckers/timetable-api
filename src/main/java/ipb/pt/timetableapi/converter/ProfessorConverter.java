package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.newData.model.Professor;
import ipb.pt.timetableapi.newData.model.Timeslot;
import ipb.pt.timetableapi.newData.repository.DepartmentRepository;
import ipb.pt.timetableapi.newData.repository.ProfessorRepository;
import ipb.pt.timetableapi.newData.repository.TimeslotRepository;
import ipb.pt.timetableapi.oldData.model.Docente;
import ipb.pt.timetableapi.oldData.repository.DocenteRepository;
import ipb.pt.timetableapi.oldData.repository.HorarioRepository;
import ipb.pt.timetableapi.oldData.repository.IndisponibilidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProfessorConverter {
//    @Autowired
//    private IndisponibilidadeRepository indisponibilidadeRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    public void convert() {
        //FIXME: indisponibilidades
//        List<Object[]> indisponibilidades = indisponibilidadeRepository.findIndisponibilidades();
        List<Object[]> indisponibilidades = horarioRepository.findIndisponibilidades();

        List<Timeslot> timeslots = timeslotRepository.findAll();

        List<Docente> docentes = docenteRepository.findAll();
        List<Professor> professors = new ArrayList<>();

        for (Docente docente : docentes) {
            Professor professor = new Professor();
            professor.setId((long) docente.getId());
            professor.setName(docente.getNome());
            professor.setAbbreviation(docente.getAbrev());

            professor.setEti(docente.getEti());
            professor.setMail(docente.getMail());
            professor.setCredito(docente.getCredito());
            professor.setIpbCodEscola(docente.getIpbCodEscola());
            professor.setIpbEmpNum(docente.getIpbEmpNum());

            professor.setDepartment(departmentRepository.findById((long) docente.getIdDepart()).orElseThrow(
                    () -> new RuntimeException("Professor [" + professor.getId() + "department not found")
            ));

            List<Object[]> indisponibilidadesProfessor = indisponibilidades.stream()
                    .filter(indisponibilidade -> (int) indisponibilidade[1] == docente.getId())
                    .toList();

            List<Timeslot> professorUnavailability = new ArrayList<>();
            for (Object[] indisponibilidade : indisponibilidadesProfessor) {
                professorUnavailability.add(timeslots.stream()
                        .filter(timeslot ->
                                timeslot.getDayOfWeek().getValue() == (int) indisponibilidade[3]
                                        && timeslot.getStartTime().equals((LocalTime) indisponibilidade[4])
                                        && timeslot.getEndTime().equals((LocalTime) indisponibilidade[5]))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Timeslot (" +
                                "day[" + DayOfWeek.of((int) indisponibilidade[3]) + "]," +
                                "startTime[" + (LocalTime) indisponibilidade[4] + "]," +
                                "startTime[" + (LocalTime) indisponibilidade[5] + "]," +
                                "not found"))
                );
            }

            professor.setUnavailability(professorUnavailability);
            professors.add(professor);
        }

        professorRepository.deleteAll();
        professorRepository.saveAll(professors);
    }
}
