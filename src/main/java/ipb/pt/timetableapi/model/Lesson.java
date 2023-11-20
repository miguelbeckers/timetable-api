package ipb.pt.timetableapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PlanningEntity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String color;
    @ManyToOne
    private Professor professor;
    @ManyToOne
    private SubjectCourse subjectCourse;
    @ManyToOne
    private SubjectType subjectType;
    @OneToMany
    @JsonIgnore
    private List<LessonResource> lessonResources = new ArrayList<>();
    @OneToMany
    @JsonIgnore
    private List<LessonStudent> lessonStudents = new ArrayList<>();

    // Initialized/Changed during planning
    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "timeslotRange")
    private Timeslot timeslot;
    @ManyToOne
    @PlanningVariable(valueRangeProviderRefs = "classroomRange")
    private Classroom classroom;
}
