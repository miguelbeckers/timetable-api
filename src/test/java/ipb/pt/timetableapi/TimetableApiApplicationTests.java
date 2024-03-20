package ipb.pt.timetableapi;

import ipb.pt.timetableapi.repository.SubjectCourseRepository;
import ipb.pt.timetableapi.repository.SubjectRepository;
import ipb.pt.timetableapi.service.LessonUnitService;
import ipb.pt.timetableapi.service.TimeslotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TimetableApiApplicationTests {
	private final SubjectRepository subjectRepository;
	private final SubjectCourseRepository subjectCourseRepository;
	private final LessonUnitService lessonUnitService;
	private final TimeslotService timeslotService;


	@Autowired
	public TimetableApiApplicationTests(SubjectRepository subjectRepository,
										SubjectCourseRepository subjectCourseRepository,
										LessonUnitService lessonUnitService,
										TimeslotService timeslotService){
		this.subjectRepository = subjectRepository;
		this.subjectCourseRepository = subjectCourseRepository;
		this.lessonUnitService = lessonUnitService;
		this.timeslotService = timeslotService;
	}
}
