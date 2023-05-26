package ipb.pt.timetableapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class Timetable {
    private List<Timeslot> timeslots;
    private List<Classroom> classrooms;
    private List<Lesson> lessons;
}
