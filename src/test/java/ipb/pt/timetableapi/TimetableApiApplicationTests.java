package ipb.pt.timetableapi;

import ipb.pt.timetableapi.model.*;
import ipb.pt.timetableapi.repository.SubjectCourseRepository;
import ipb.pt.timetableapi.repository.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ipb.pt.timetableapi.service.LessonUnitService.splitBlocks;
import static ipb.pt.timetableapi.solver.TimetableConstraintProvider.checkIfTheLessonsAreOutOfTheBlock;

@SpringBootTest
class TimetableApiApplicationTests {
	private final SubjectRepository subjectRepository;
	private final SubjectCourseRepository subjectCourseRepository;


	@Autowired
	public TimetableApiApplicationTests(SubjectRepository subjectRepository,
										SubjectCourseRepository subjectCourseRepository){
		this.subjectRepository = subjectRepository;
		this.subjectCourseRepository = subjectCourseRepository;
	}

	void createTestCheckIfTheLessonsAreOutOfTheBlock(String lesson1StartTime, String lesson2StartTime, boolean expected) {
		LessonUnit lessonUnit1 = new LessonUnit();
		LessonUnit lessonUnit2 = new LessonUnit();

		Lesson lesson = new Lesson();
		lesson.setBlocks(2);
		lesson.setHoursPerWeek(3D);

		Timeslot timeslot1 = new Timeslot();
		Timeslot timeslot2 = new Timeslot();

		timeslot1.setStartTime(LocalTime.parse(lesson1StartTime));
		timeslot2.setStartTime(LocalTime.parse(lesson2StartTime));

		lessonUnit1.setTimeslot(timeslot1);
		lessonUnit2.setTimeslot(timeslot2);

		lessonUnit1.setLesson(lesson);
		lessonUnit2.setLesson(lesson);

		if (expected) {
			Assert.isTrue(
					checkIfTheLessonsAreOutOfTheBlock(lessonUnit1, lessonUnit2),
					"The lessons are out of the block"
			);
		} else {
			Assert.isTrue(
					!checkIfTheLessonsAreOutOfTheBlock(lessonUnit1, lessonUnit2),
					"The lessons are not out of the block"
			);
		}
	}

	@Test
	void testCheckIfTheLessonsAreOutOfTheBlock() {
		createTestCheckIfTheLessonsAreOutOfTheBlock("08:00", "10:00", true);
		createTestCheckIfTheLessonsAreOutOfTheBlock("08:00", "09:00", false);
	}

	@Test
	void testCheckBlockSizeDivision() {
		Timeslot timeslot = new Timeslot();
		timeslot.setStartTime(LocalTime.parse("08:00"));
		timeslot.setEndTime(LocalTime.parse("13:00"));
		timeslot.setDayOfWeek(DayOfWeek.of(1));

		LessonUnit lessonUnit = new LessonUnit();

		lessonUnit.setId(1L);
		lessonUnit.setTimeslot(timeslot);
		lessonUnit.setBlockSize(5.0);

		List<LessonUnit> lessonUnits = splitBlocks(List.of(lessonUnit), 2.5);

//		08:00->08:30 ┌─────────┐ ┌─────────┐
//		08:30->09:00 │         │ │         │
//		09:00->09:30 │         │ │   2.5   │
//		09:30->10:00 │         │ │         │
//		10:00->10:30 │    5    │ └─────────┘
//		10:30->11:00 │         │ ┌─────────┐
//		11:00->11:30 │         │ │         │
//		11:30->12:00 │         │ │   2.5   │
//		12:00->12:30 │         │ │         │
//		12:30->13:00 └─────────┘ └─────────┘

		Assert.isTrue(lessonUnits.size() == 2,
				"The lesson unit was split into 2 lesson units");

//		Assert.isTrue(lessonUnits.get(0).getBlockSize() == 2.5,
//				"The first lesson unit has a block size of 2.5");
//
//		Assert.isTrue(lessonUnits.get(0).getTimeslot().getStartTime().equals(LocalTime.parse("08:00")),
//				"The first lesson unit starts at 08:00");
//
//		Assert.isTrue(lessonUnits.get(0).getTimeslot().getEndTime().equals(LocalTime.parse("10:30")),
//				"The first lesson unit ends at 10:30");
//
//		Assert.isTrue(lessonUnits.get(0).getTimeslot().getDayOfWeek().equals(timeslot.getDayOfWeek()),
//				"The first lesson unit has the same day of the week as the timeslot");
//
//		Assert.isTrue(lessonUnits.get(1).getBlockSize() == 2.5,
//				"The second lesson unit has a block size of 2.5");
//
//		Assert.isTrue(lessonUnits.get(1).getTimeslot().getStartTime().equals(LocalTime.parse("10:30")),
//				"The second lesson unit starts at 10:30");
//
//		Assert.isTrue(lessonUnits.get(1).getTimeslot().getEndTime().equals(LocalTime.parse("13:00")),
//				"The second lesson unit ends at 13:00");
//
//		Assert.isTrue(lessonUnits.get(1).getTimeslot().getDayOfWeek().equals(timeslot.getDayOfWeek()),
//				"The second lesson unit has the same day of the week as the timeslot");
//
//		Assert.isTrue(lessonUnits.get(0).getTimeslot().getDayOfWeek().equals(timeslot.getDayOfWeek()),
//				"The second lesson unit has the same day of the week as the timeslot");
	}

	@Test
	void testCheckBlockSizeDivision2() {

		List<Subject> subjects = subjectRepository.findAll();

		//create a hasMap
		HashMap<Long, Subject> subjectHashMap = new HashMap<>();
		for (Subject subject : subjects) {
			subjectHashMap.put(subject.getId(), subject);
		}

		Subject subject = subjectHashMap.get(742L);

		System.out.println(subject.getName());

		List<SubjectCourse> subjectCourses = subjectCourseRepository.findAll();
		HashMap<Long, SubjectCourse> subjectCourseHashMap = new HashMap<>();
		for (SubjectCourse subjectCourse : subjectCourses) {
			subjectCourseHashMap.put(subjectCourse.getId(), subjectCourse);
		}

		SubjectCourse subjectCourse = subjectCourseHashMap.get(1L);
	}
}
