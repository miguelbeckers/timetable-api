package ipb.pt.timetableapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student {
    @Id
    private Long id;
    @OneToMany(fetch = FetchType.EAGER)
    private List<SubjectCourse> subjectCourses = new ArrayList<>();
}
