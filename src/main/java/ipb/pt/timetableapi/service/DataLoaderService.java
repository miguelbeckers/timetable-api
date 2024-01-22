package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.*;
import ipb.pt.timetableapi.model.ClassroomResource;
import ipb.pt.timetableapi.model.LessonUnit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoaderService {

    public void persistAll(
            List<SubjectDto> subjectResponse,
            List<PeriodDto> periodResponse,
            List<ResourceDto> resourceResponse,
            List<DepartmentDto> departmentResponse,
            List<SubjectTypeDto> subjectTypeResponse,
            List<TimeslotDto> timeslotResponse,
            List<ProfessorDto> professorResponse,
            List<CourseDto> courseResponse,
            List<ClassroomResourceDto> classroomResourceResponse,
            List<ClassroomDto> classroomResponse,
            List<LessonResourceDto> lessonResourceResponse,
            List<SubjectCourseDto> subjectCourseResponse,
            List<StudentDto> studentResponse,
            List<LessonDto> lessonResponse,
            List<LessonUnitDto> lessonUnitResponse
    ) {

    }
}
