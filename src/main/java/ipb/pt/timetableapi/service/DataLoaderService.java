package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.*;
import ipb.pt.timetableapi.model.*;
import ipb.pt.timetableapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataLoaderService {
    private final SubjectRepository subjectRepository;
    private final PeriodRepository periodRepository;
    private final ResourceRepository resourceRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectTypeRepository subjectTypeRepository;
    private final TimeslotRepository timeslotRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final ClassroomResourceRepository classroomResourceRepository;
    private final ClassroomRepository classroomRepository;
    private final LessonResourceRepository lessonResourceRepository;
    private final SubjectCourseRepository subjectCourseRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final LessonUnitRepository lessonUnitRepository;

    @Autowired
    public DataLoaderService(
            SubjectRepository subjectRepository,
            PeriodRepository periodRepository,
            ResourceRepository resourceRepository,
            DepartmentRepository departmentRepository,
            SubjectTypeRepository subjectTypeRepository,
            TimeslotRepository timeslotRepository,
            ProfessorRepository professorRepository,
            CourseRepository courseRepository,
            ClassroomResourceRepository classroomResourceRepository,
            ClassroomRepository classroomRepository,
            LessonResourceRepository lessonResourceRepository,
            SubjectCourseRepository subjectCourseRepository,
            StudentRepository studentRepository,
            LessonRepository lessonRepository,
            LessonUnitRepository lessonUnitRepository
    ) {
        this.subjectRepository = subjectRepository;
        this.periodRepository = periodRepository;
        this.resourceRepository = resourceRepository;
        this.departmentRepository = departmentRepository;
        this.subjectTypeRepository = subjectTypeRepository;
        this.timeslotRepository = timeslotRepository;
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
        this.classroomResourceRepository = classroomResourceRepository;
        this.classroomRepository = classroomRepository;
        this.lessonResourceRepository = lessonResourceRepository;
        this.subjectCourseRepository = subjectCourseRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.lessonUnitRepository = lessonUnitRepository;
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

        List<Subject> subjects = new ArrayList<>();
        List<Period> periods = new ArrayList<>();
        List<Resource> resources = new ArrayList<>();
        List<Department> departments = new ArrayList<>();
        List<SubjectType> subjectTypes = new ArrayList<>();
        List<Timeslot> timeslots = new ArrayList<>();
        List<Professor> professors = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<ClassroomResource> classroomResources = new ArrayList<>();
        List<Classroom> classrooms = new ArrayList<>();
        List<LessonResource> lessonResources = new ArrayList<>();
        List<SubjectCourse> subjectCourses = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Lesson> lessons = new ArrayList<>();
        List<LessonUnit> lessonUnits = new ArrayList<>();

        subjectRepository.saveAll(subjects);
        periodRepository.saveAll(periods);
        resourceRepository.saveAll(resources);
        departmentRepository.saveAll(departments);
        subjectTypeRepository.saveAll(subjectTypes);
        timeslotRepository.saveAll(timeslots);
        professorRepository.saveAll(professors);
        courseRepository.saveAll(courses);
        classroomResourceRepository.saveAll(classroomResources);
        classroomRepository.saveAll(classrooms);
        lessonResourceRepository.saveAll(lessonResources);
        subjectCourseRepository.saveAll(subjectCourses);
        studentRepository.saveAll(students);
        lessonRepository.saveAll(lessons);
        lessonUnitRepository.saveAll(lessonUnits);
    }
}
