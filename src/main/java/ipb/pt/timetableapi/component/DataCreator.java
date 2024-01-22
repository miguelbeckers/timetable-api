package ipb.pt.timetableapi.component;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataCreator implements ApplicationRunner {
//    @Autowired
//    public ClassroomRepository classroomRepository;
//
//    @Autowired
//    public LessonRepository lessonRepository;
//
//    @Autowired
//    public ProfessorRepository professorRepository;
//
//    @Autowired
//    public TimeslotRepository timeslotRepository;
//
//    @Autowired
//    public SubjectRepository subjectRepository;
//
//    @Autowired
//    public StudentRepository studentRepository;
//
//    @Autowired
//    public LessonStudentRepository lessonStudentRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("DataInitializer.run");
//
//        Timeslot timeslot1 = new Timeslot();
//        Timeslot timeslot2 = new Timeslot();
//        timeslot1.setDayOfWeek(DayOfWeek.MONDAY);
//        timeslot1.setStartTime(LocalTime.parse("08:00"));
//        timeslot1.setEndTime(LocalTime.parse("09:00"));
//        timeslot2.setDayOfWeek(DayOfWeek.MONDAY);
//        timeslot2.setStartTime(LocalTime.parse("09:00"));
//        timeslot2.setEndTime(LocalTime.parse("10:00"));
//        Timeslot timeslot1Saved = timeslotRepository.save(timeslot1);
//        Timeslot timeslot2Saved = timeslotRepository.save(timeslot2);
//
//        Classroom classroom1 = new Classroom();
//        classroom1.setName("Room A");
//        Classroom classroom1Saved = classroomRepository.save(classroom1);
//
//        Classroom classroom2 = new Classroom();
//        classroom2.setName("Room B");
//        Classroom classroom2Saved = classroomRepository.save(classroom2);
//
//        Student student1 = new Student();
//        student1.setCode("A");
//        Student student1Saved = studentRepository.save(student1);
//
//        Lesson lesson1 = new Lesson();
//        Lesson lesson2 = new Lesson();
//        Lesson lesson1Saved = lessonRepository.save(lesson1);
//        Lesson lesson2Saved = lessonRepository.save(lesson2);
//
//        LessonStudent lessonStudent1 = new LessonStudent();
//        LessonStudent lessonStudent2 = new LessonStudent();
//        lessonStudent1.setLesson(lesson1Saved);
//        lessonStudent1.setStudent(student1Saved);
//        lessonStudent2.setLesson(lesson2Saved);
//        lessonStudent2.setStudent(student1Saved);
//        LessonStudent lessonStudent1Saved = lessonStudentRepository.save(lessonStudent1);
//        LessonStudent lessonStudent2Saved = lessonStudentRepository.save(lessonStudent2);
//
//        lesson1Saved.setLessonStudents(List.of(lessonStudent1Saved));
//        lesson2Saved.setLessonStudents(List.of(lessonStudent2Saved));
//        Lesson lesson1Updated = lessonRepository.save(lesson1Saved);
//        Lesson lesson2Updated = lessonRepository.save(lesson2Saved);

//
//        List<Classroom> classrooms = classroomRepository.saveAll(List.of(
//                new Classroom("Room J", 30),
//                new Classroom("Room A", 30),
//                new Classroom("Room B", 30),
//                new Classroom("Room C", 30),
//                new Classroom("Room D", 30),
//                new Classroom("Room E", 30),
//                new Classroom("Room F", 30),
//                new Classroom("Room G", 30),
//                new Classroom("Room H", 30)
//        ));
//
//        System.out.println("classrooms: " + classrooms);
//
//        List<Professor> professors = professorRepository.saveAll(List.of(
//                new Professor("Professor A"),
//                new Professor("Professor B"),
//                new Professor("Professor C"),
//                new Professor("Professor D"),
//                new Professor("Professor E"),
//                new Professor("Professor F"),
//                new Professor("Professor G"),
//                new Professor("Professor H"),
//                new Professor("Professor I"),
//                new Professor("Professor J")
//        ));
//
//        System.out.println("professors: " + professors);
//
//        List<Lesson> lessons = lessonRepository.saveAll(List.of(
//                new Lesson("Math", professors.get(0), "Group A", "#ff7875", 20),
//                new Lesson("Math", professors.get(0), "Group B", "#ff7875", 20),
//                new Lesson("Math", professors.get(0), "Group C", "#ff7875", 20),
//                new Lesson("Math", professors.get(0), "Group D", "#ff7875", 20),
//                new Lesson("Math", professors.get(4), "Group E", "#ff7875", 20),
//                new Lesson("Math", professors.get(4), "Group F", "#ff7875", 20),
//                new Lesson("Math", professors.get(4), "Group G", "#ff7875", 20),
//                new Lesson("Math", professors.get(4), "Group H", "#ff7875", 20),
//                new Lesson("Math", professors.get(4), "Group I", "#ff7875", 20),
//                new Lesson("Math", professors.get(4), "Group J", "#ff7875", 20),
//                new Lesson("Physics", professors.get(1), "Group A", "#4096ff", 20),
//                new Lesson("Physics", professors.get(1), "Group B", "#4096ff", 20),
//                new Lesson("Physics", professors.get(1), "Group C", "#4096ff", 20),
//                new Lesson("Physics", professors.get(5), "Group D", "#4096ff", 20),
//                new Lesson("Physics", professors.get(5), "Group E", "#4096ff", 20),
//                new Lesson("Physics", professors.get(5), "Group F", "#4096ff", 20),
//                new Lesson("Physics", professors.get(5), "Group G", "#4096ff", 20),
//                new Lesson("Geography", professors.get(3), "Group A", "#bae637", 20),
//                new Lesson("Geography", professors.get(3), "Group B", "#bae637", 20),
//                new Lesson("Geography", professors.get(3), "Group C", "#bae637", 20),
//                new Lesson("Geography", professors.get(3), "Group D", "#bae637", 20),
//                new Lesson("Geography", professors.get(3), "Group E", "#bae637", 20),
//                new Lesson("Geography", professors.get(6), "Group F", "#bae637", 20),
//                new Lesson("Geography", professors.get(6), "Group G", "#bae637", 20),
//                new Lesson("Geography", professors.get(6), "Group H", "#bae637", 20),
//                new Lesson("Geography", professors.get(6), "Group I", "#bae637", 20),
//                new Lesson("Geography", professors.get(6), "Group J", "#bae637", 20)
//        ));
//
//        System.out.println("lessons: " + lessons);
//
//        List<Timeslot> timeslots = timeslotRepository.saveAll(List.of(
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("08:00"), LocalTime.parse("09:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("09:00"), LocalTime.parse("10:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("10:00"), LocalTime.parse("11:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("11:00"), LocalTime.parse("12:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("12:00"), LocalTime.parse("13:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("13:00"), LocalTime.parse("14:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("14:00"), LocalTime.parse("15:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("15:00"), LocalTime.parse("16:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("16:00"), LocalTime.parse("17:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("17:00"), LocalTime.parse("18:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("18:00"), LocalTime.parse("19:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("19:00"), LocalTime.parse("20:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("20:00"), LocalTime.parse("21:00")),
//                new Timeslot(DayOfWeek.MONDAY, LocalTime.parse("21:00"), LocalTime.parse("22:00"))
//        ));
//
//        System.out.println("timeslots: " + timeslots);
    }
}
