package ipb.pt.timetableapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/data")
public class DataLoaderController {
    private final SubjectController subjectController;
    private final PeriodController periodController;
    private final ResourceController resourceController;
    private final DepartmentController departmentController;
    private final SubjectTypeController subjectTypeController;
    private final TimeslotController timeslotController;
    private final ProfessorController professorController;
    private final CourseController courseController;
    private final ClassroomResourceController classroomResourceController;
    private final ClassroomController classroomController;
    private final LessonResourceController lessonResourceController;
    private final SubjectCourseController subjectCourseController;
    private final StudentController studentController;
    private final LessonController lessonController;
    private final LessonUnitController lessonUnitController;

    @Autowired
    public DataLoaderController(
            SubjectController subjectController,
            PeriodController periodController,
            ResourceController resourceController,
            DepartmentController departmentController,
            SubjectTypeController subjectTypeController,
            TimeslotController timeslotController,
            ProfessorController professorController,
            CourseController courseController,
            ClassroomResourceController classroomResourceController,
            ClassroomController classroomController,
            LessonResourceController lessonResourceController,
            SubjectCourseController subjectCourseController,
            StudentController studentController,
            LessonController lessonController,
            LessonUnitController lessonUnitController
    ) {
        this.subjectController = subjectController;
        this.periodController = periodController;
        this.resourceController = resourceController;
        this.departmentController = departmentController;
        this.subjectTypeController = subjectTypeController;
        this.timeslotController = timeslotController;
        this.professorController = professorController;
        this.courseController = courseController;
        this.classroomResourceController = classroomResourceController;
        this.classroomController = classroomController;
        this.lessonResourceController = lessonResourceController;
        this.subjectCourseController = subjectCourseController;
        this.studentController = studentController;
        this.lessonController = lessonController;
        this.lessonUnitController = lessonUnitController;
    }


    @GetMapping("/load")
    public ResponseEntity<Object> load() throws JsonProcessingException {
        subjectController.load();
        periodController.load();
        resourceController.load();
        departmentController.load();
        subjectTypeController.load();
        timeslotController.load();
        professorController.load();
        courseController.load();
        classroomResourceController.load();
        classroomController.load();
        lessonResourceController.load();
        subjectCourseController.load();
        studentController.load();
        lessonController.load();
        lessonUnitController.load();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/load2")
    public ResponseEntity<Object> load2() throws JsonProcessingException {
        subjectController.load();
        periodController.load();
        resourceController.load();
        departmentController.load();
        subjectTypeController.load();
        timeslotController.load();
        professorController.load();
        courseController.load();
        classroomResourceController.load();
        classroomController.load();
        lessonResourceController.load();
        subjectCourseController.load();
        studentController.load();
        lessonController.load();
        lessonUnitController.load();
        return ResponseEntity.ok().build();
    }
}
