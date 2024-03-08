package ipb.pt.timetableapi.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentDto {
    private Long id;
    private List<Long> subjectCourseIds = new ArrayList<>();
}
