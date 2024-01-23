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
    @ManyToOne(cascade = CascadeType.ALL)
    private SubjectCourse subjectCourse;
    @ManyToOne(cascade = CascadeType.ALL)
    private SubjectType subjectType;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Professor> professors = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonResource> lessonResources = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students = new ArrayList<>();
}
