package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataLoaderService {
    private final SubjectService subjectService;
    private final PeriodService periodService;
    private final ResourceService resourceService;
    private final DepartmentService departmentService;
    private final SubjectTypeService subjectTypeService;
    private final TimeslotService timeslotService;
    private final ProfessorService professorService;
    private final CourseService courseService;
    private final ClassroomResourceService classroomResourceService;
    private final ClassroomService classroomService;
    private final LessonResourceService lessonResourceService;
    private final SubjectCourseService subjectCourseService;
    private final StudentService studentService;
    private final LessonService lessonService;
    private final LessonUnitService lessonUnitService;

    @Autowired
    public DataLoaderService(
            SubjectService subjectService,
            PeriodService periodService,
            ResourceService resourceService,
            DepartmentService departmentService,
            SubjectTypeService subjectTypeService,
            TimeslotService timeslotService,
            ProfessorService professorService,
            CourseService courseService,
            ClassroomResourceService classroomResourceService,
            ClassroomService classroomService,
            LessonResourceService lessonResourceService,
            SubjectCourseService subjectCourseService,
            StudentService studentService,
            LessonService lessonService,
            LessonUnitService lessonUnitService
    ) {
        this.subjectService = subjectService;
        this.periodService = periodService;
        this.resourceService = resourceService;
        this.departmentService = departmentService;
        this.subjectTypeService = subjectTypeService;
        this.timeslotService = timeslotService;
        this.professorService = professorService;
        this.courseService = courseService;
        this.classroomResourceService = classroomResourceService;
        this.classroomService = classroomService;
        this.lessonResourceService = lessonResourceService;
        this.subjectCourseService = subjectCourseService;
        this.studentService = studentService;
        this.lessonService = lessonService;
        this.lessonUnitService = lessonUnitService;
    }

    public void persistAll(
            List<SubjectDto> subjectDtos,
            List<PeriodDto> periodDtos,
            List<ResourceDto> resourceDtos,
            List<DepartmentDto> departmentDtos,
            List<SubjectTypeDto> subjectTypeDtos,
            List<TimeslotDto> timeslotDtos,
            List<ProfessorDto> professorDtos,
            List<CourseDto> courseDtos,
            List<ClassroomResourceDto> classroomResourceDtos,
            List<ClassroomDto> classroomDtos,
            List<LessonResourceDto> lessonResourceDtos,
            List<SubjectCourseDto> subjectCourseDtos,
            List<StudentDto> studentDtos,
            List<LessonDto> lessonDtos,
            List<LessonUnitDto> lessonUnitDtos
    ) {
        subjectService.createMany(subjectDtos);
        periodService.createMany(periodDtos);
        resourceService.createMany(resourceDtos);
        departmentService.createMany(departmentDtos);
        subjectTypeService.createMany(subjectTypeDtos);
        timeslotService.createMany(timeslotDtos);
        professorService.createMany(professorDtos);
        courseService.createMany(courseDtos);
        classroomResourceService.createMany(classroomResourceDtos);
        classroomService.createMany(classroomDtos);
        lessonResourceService.createMany(lessonResourceDtos);
        subjectCourseService.createMany(subjectCourseDtos);
        studentService.createMany(studentDtos);
        lessonService.createMany(lessonDtos);
        lessonUnitService.createMany(lessonUnitDtos);
    }
}
