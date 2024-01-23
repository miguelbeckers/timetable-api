package ipb.pt.timetableapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private SubjectCourse subjectCourse;
    @ManyToOne
    private SubjectType subjectType;
    @ManyToMany
    private List<LessonResource> lessonResources = new ArrayList<>();
    @ManyToMany
    private List<Professor> professors = new ArrayList<>();
    @ManyToMany
    private List<Student> students = new ArrayList<>();
}
