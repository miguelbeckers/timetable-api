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
    private final ClassroomTypeService classroomTypeService;
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
            ClassroomTypeService classroomTypeService,
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
        this.classroomTypeService = classroomTypeService;
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
            List<ClassroomTypeDto> classroomTypeDtos,
            List<ClassroomDto> classroomDtos,
            List<LessonResourceDto> lessonResourceDtos,
            List<SubjectCourseDto> subjectCourseDtos,
            List<StudentDto> studentDtos,
            List<LessonDto> lessonDtos,
            List<LessonUnitDto> lessonUnitDtos
    ) {
        lessonUnitService.deleteAll();
        lessonService.deleteAll();
        studentService.deleteAll();
        subjectCourseService.deleteAll();
        lessonResourceService.deleteAll();
        classroomService.deleteAll();
        classroomResourceService.deleteAll();
        courseService.deleteAll();
        professorService.deleteAll();
        timeslotService.deleteAll();
        subjectTypeService.deleteAll();
        departmentService.deleteAll();
        resourceService.deleteAll();
        periodService.deleteAll();
        subjectService.deleteAll();

        subjectService.saveAll(subjectDtos);
        periodService.saveAll(periodDtos);
        resourceService.saveAll(resourceDtos);
        departmentService.saveAll(departmentDtos);
        subjectTypeService.saveAll(subjectTypeDtos);
        timeslotService.saveAll(timeslotDtos);
        professorService.saveAll(professorDtos);
        courseService.saveAll(courseDtos);
        classroomResourceService.saveAll(classroomResourceDtos);
        classroomTypeService.saveAll(classroomTypeDtos);
        classroomService.saveAll(classroomDtos);
        lessonResourceService.saveAll(lessonResourceDtos);
        subjectCourseService.saveAll(subjectCourseDtos);
        studentService.saveAll(studentDtos);
        lessonService.saveAll(lessonDtos);
        lessonUnitService.saveAll(lessonUnitDtos);

        System.out.println("Data persisted successfully!");
    }
}
