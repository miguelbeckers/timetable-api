package ipb.pt.timetableapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lesson {
    @Id
    private Long id;
    private String name;
    private String color;
    private Double hoursPerWeek;
    private Integer blocks;
    @ManyToOne
    private Professor professor;
    @ManyToOne
    private SubjectCourse subjectCourse;
    @ManyToOne
    private SubjectType subjectType;
    @OneToMany
    private List<LessonResource> lessonResources = new ArrayList<>();
    @OneToMany
    private List<Student> students = new ArrayList<>();
}
