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
    private Integer groupNumber;
    private Integer groupCount;
    private String color;
    private Double hoursPerWeek;
    private Integer blocks;
    @ManyToOne
    private SubjectCourse subjectCourse;
    @ManyToOne
    private SubjectType subjectType;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<LessonResource> lessonResources = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Professor> professors = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Student> students = new ArrayList<>();
}
