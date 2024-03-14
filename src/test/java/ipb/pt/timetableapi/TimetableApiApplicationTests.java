package ipb.pt.timetableapi;

import ipb.pt.timetableapi.model.*;
import ipb.pt.timetableapi.repository.SubjectCourseRepository;
import ipb.pt.timetableapi.repository.SubjectRepository;
import ipb.pt.timetableapi.service.LessonUnitService;
import ipb.pt.timetableapi.service.TimeslotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//import static ipb.pt.timetableapi.service.LessonUnitService.splitBlocks;
import static ipb.pt.timetableapi.solver.TimetableConstraintProvider.checkIfTheLessonsAreOutOfTheBlock;

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

//	@Test
//	void testCheckBlockSizeDivision() {
//		Timeslot timeslot = new Timeslot();
//		timeslot.setStartTime(LocalTime.parse("08:00"));
//		timeslot.setEndTime(LocalTime.parse("13:00"));
//		timeslot.setDayOfWeek(DayOfWeek.of(1));
//
//		LessonUnit lessonUnit = new LessonUnit();
//
//		lessonUnit.setId(1L);
//		lessonUnit.setTimeslot(timeslot);
//		lessonUnit.setBlockSize(5.0);
//
//		List<LessonUnit> lessonUnits = splitBlocks(List.of(lessonUnit), 2.5);
//
////		08:00->08:30 ┌─────────┐ ┌─────────┐
////		08:30->09:00 │         │ │         │
////		09:00->09:30 │         │ │   2.5   │
////		09:30->10:00 │         │ │         │
////		10:00->10:30 │    5    │ └─────────┘
////		10:30->11:00 │         │ ┌─────────┐
////		11:00->11:30 │         │ │         │
////		11:30->12:00 │         │ │   2.5   │
////		12:00->12:30 │         │ │         │
////		12:30->13:00 └─────────┘ └─────────┘
//
//		Assert.isTrue(lessonUnits.size() == 2,
//				"The lesson unit was split into 2 lesson units");

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
//	}

	@Test
	void testGetLessonUnitsAsBlocks() {
		Lesson lesson1 = new Lesson();
		lesson1.setId(1L);
		lesson1.setBlocks(2);
		lesson1.setHoursPerWeek(3D);

		LessonUnit lessonUnit1 = new LessonUnit();
		LessonUnit lessonUnit2 = new LessonUnit();
		LessonUnit lessonUnit3 = new LessonUnit();
		LessonUnit lessonUnit4 = new LessonUnit();
		LessonUnit lessonUnit5 = new LessonUnit();
		LessonUnit lessonUnit6 = new LessonUnit();

		lessonUnit1.setId(1L);
		lessonUnit1.setLesson(lesson1);

		lessonUnit2.setId(2L);
		lessonUnit2.setLesson(lesson1);

		lessonUnit3.setId(3L);
		lessonUnit3.setLesson(lesson1);

		lessonUnit4.setId(4L);
		lessonUnit4.setLesson(lesson1);

		lessonUnit5.setId(5L);
		lessonUnit5.setLesson(lesson1);

		lessonUnit6.setId(6L);
		lessonUnit6.setLesson(lesson1);

		List<LessonUnit> lessonUnits = new ArrayList<>();
		lessonUnits.add(lessonUnit1);
		lessonUnits.add(lessonUnit2);
		lessonUnits.add(lessonUnit3);
		lessonUnits.add(lessonUnit4);
		lessonUnits.add(lessonUnit5);
		lessonUnits.add(lessonUnit6);

		List<LessonUnit> newLessonUnits = lessonUnitService.getLessonUnitsAsBlocks(lessonUnits);

		Assert.isTrue(newLessonUnits.size() == 2,
				"The new lesson1 units has the size of 2");

		Assert.isTrue(newLessonUnits.get(0).getId() == 1L,
				"The first object has the id of 1");

		Assert.isTrue(newLessonUnits.get(1).getId() == 4L,
				"The second object has the id of 4");

		Assert.isTrue(newLessonUnits.get(0).getBlockSize() == 1.5,
				"The first object has the blockSize of 1.5");

		Assert.isTrue(newLessonUnits.get(1).getBlockSize() == 1.5,
				"The second object has the blockSize of 1.5");

		Lesson lesson2 = new Lesson();
		lesson2.setId(2L);
		lesson2.setBlocks(1);
		lesson2.setHoursPerWeek(2D);

		LessonUnit lessonUnit7 = new LessonUnit();
		LessonUnit lessonUnit8 = new LessonUnit();
		LessonUnit lessonUnit9 = new LessonUnit();
		LessonUnit lessonUnit10 = new LessonUnit();

		lessonUnit7.setId(7L);
		lessonUnit7.setLesson(lesson2);

		lessonUnit8.setId(8L);
		lessonUnit8.setLesson(lesson2);

		lessonUnit9.setId(9L);
		lessonUnit9.setLesson(lesson2);

		lessonUnit10.setId(10L);
		lessonUnit10.setLesson(lesson2);

		lessonUnits.add(lessonUnit1);
		lessonUnits.add(lessonUnit2);
		lessonUnits.add(lessonUnit3);
		lessonUnits.add(lessonUnit4);
		lessonUnits.add(lessonUnit5);
		lessonUnits.add(lessonUnit6);
		lessonUnits.add(lessonUnit7);
		lessonUnits.add(lessonUnit8);
		lessonUnits.add(lessonUnit9);
		lessonUnits.add(lessonUnit10);

		newLessonUnits = lessonUnitService.getLessonUnitsAsBlocks(lessonUnits);

		Assert.isTrue(newLessonUnits.size() == 3,
				"The new lessons units has the size of 3");

		Assert.isTrue(newLessonUnits.get(2).getId() == 7L,
				"The third object has the id of 7");

		Assert.isTrue(newLessonUnits.get(2).getBlockSize() == 2,
				"The third object has the blockSize of 2");
	}

	@Test
	void testSplitBlocksOfLessonUnits() {
		Lesson lesson1 = new Lesson();
		lesson1.setId(1L);
		lesson1.setBlocks(2);
		lesson1.setHoursPerWeek(3D);

		LessonUnit lessonUnit1 = new LessonUnit();
		LessonUnit lessonUnit2 = new LessonUnit();
		LessonUnit lessonUnit3 = new LessonUnit();
		LessonUnit lessonUnit4 = new LessonUnit();
		LessonUnit lessonUnit5 = new LessonUnit();
		LessonUnit lessonUnit6 = new LessonUnit();

		lessonUnit1.setId(1L);
		lessonUnit1.setLesson(lesson1);

		lessonUnit2.setId(2L);
		lessonUnit2.setLesson(lesson1);

		lessonUnit3.setId(3L);
		lessonUnit3.setLesson(lesson1);

		lessonUnit4.setId(4L);
		lessonUnit4.setLesson(lesson1);

		lessonUnit5.setId(5L);
		lessonUnit5.setLesson(lesson1);

		lessonUnit6.setId(6L);
		lessonUnit6.setLesson(lesson1);

		Lesson lesson2 = new Lesson();
		lesson2.setId(2L);
		lesson2.setBlocks(1);
		lesson2.setHoursPerWeek(2D);

		LessonUnit lessonUnit7 = new LessonUnit();
		LessonUnit lessonUnit8 = new LessonUnit();
		LessonUnit lessonUnit9 = new LessonUnit();
		LessonUnit lessonUnit10 = new LessonUnit();

		lessonUnit7.setId(7L);
		lessonUnit7.setLesson(lesson2);

		lessonUnit8.setId(8L);
		lessonUnit8.setLesson(lesson2);

		lessonUnit9.setId(9L);
		lessonUnit9.setLesson(lesson2);

		lessonUnit10.setId(10L);
		lessonUnit10.setLesson(lesson2);

		List<LessonUnit> lessonUnits = new ArrayList<>();
		lessonUnits.add(lessonUnit1);
		lessonUnits.add(lessonUnit2);
		lessonUnits.add(lessonUnit3);
		lessonUnits.add(lessonUnit4);
		lessonUnits.add(lessonUnit5);
		lessonUnits.add(lessonUnit6);
		lessonUnits.add(lessonUnit7);
		lessonUnits.add(lessonUnit8);
		lessonUnits.add(lessonUnit9);
		lessonUnits.add(lessonUnit10);

		List<LessonUnit> lessonUnitsAsBlocks = lessonUnitService.getLessonUnitsAsBlocks(lessonUnits);
//		List<LessonUnit> blocksOf1 = lessonUnitService.splitBlocks(lessonUnitsAsBlocks, 1);
//
//		Assert.isTrue(blocksOf1.size() == 6,
//				"The size of the blocksOf1 list should be 6");
//
//		Assert.isTrue(blocksOf1.get(0).getBlockSize() == 1,
//				"The size of the first block should be 1");
//
//		Assert.isTrue(blocksOf1.get(0).getId() == 1L,
//				"The id of the first block should be 1");
//
//		Assert.isTrue(blocksOf1.get(1).getBlockSize() == 0.5,
//				"The size of the second block should be 1");
//
//		Assert.isTrue(blocksOf1.get(1).getId() == 3L,
//				"The id of the second block should be 3");
//
//		Assert.isTrue(blocksOf1.get(2).getBlockSize() == 1,
//				"The size of the third block should be 1");
//
//		Assert.isTrue(blocksOf1.get(2).getId() == 4L,
//				"The id of the third block should be 4");
//
//		Assert.isTrue(blocksOf1.get(3).getBlockSize() == 0.5,
//				"The size of the fourth block should be 1");
//
//		Assert.isTrue(blocksOf1.get(3).getId() == 6L,
//				"The id of the fourth block should be 6");
//
//		Assert.isTrue(blocksOf1.get(4).getBlockSize() == 1,
//				"The size of the fifth block should be 1");
//
//		Assert.isTrue(blocksOf1.get(4).getId() == 7L,
//				"The id of the fifth block should be 7");
//
//		Assert.isTrue(blocksOf1.get(5).getBlockSize() == 1,
//				"The size of the sixth block should be 1");
//
//		Assert.isTrue(blocksOf1.get(5).getId() == 9L,
//				"The id of the sixth block should be 9");
	}

	@Test
	void testSplitTimeslots() {
		Timeslot timeslot1 = new Timeslot();
		Timeslot timeslot2 = new Timeslot();
		Timeslot timeslot3 = new Timeslot();
		Timeslot timeslot4 = new Timeslot();
		Timeslot timeslot5 = new Timeslot();
		Timeslot timeslot6 = new Timeslot();
		Timeslot timeslot7 = new Timeslot();
		Timeslot timeslot8 = new Timeslot();
		Timeslot timeslot9 = new Timeslot();
		Timeslot timeslot10 = new Timeslot();

		timeslot1.setStartTime(LocalTime.parse("08:00"));
		timeslot1.setEndTime(LocalTime.parse("08:30"));

		timeslot2.setStartTime(LocalTime.parse("08:30"));
		timeslot2.setEndTime(LocalTime.parse("09:00"));

		timeslot3.setStartTime(LocalTime.parse("09:00"));
		timeslot3.setEndTime(LocalTime.parse("09:30"));

		timeslot4.setStartTime(LocalTime.parse("09:30"));
		timeslot4.setEndTime(LocalTime.parse("10:00"));

		timeslot5.setStartTime(LocalTime.parse("10:00"));
		timeslot5.setEndTime(LocalTime.parse("10:30"));

		timeslot6.setStartTime(LocalTime.parse("10:30"));
		timeslot6.setEndTime(LocalTime.parse("11:00"));

		timeslot7.setStartTime(LocalTime.parse("11:00"));
		timeslot7.setEndTime(LocalTime.parse("11:30"));

		timeslot8.setStartTime(LocalTime.parse("11:30"));
		timeslot8.setEndTime(LocalTime.parse("12:00"));

		timeslot9.setStartTime(LocalTime.parse("12:00"));
		timeslot9.setEndTime(LocalTime.parse("12:30"));

		timeslot10.setStartTime(LocalTime.parse("12:30"));
		timeslot10.setEndTime(LocalTime.parse("13:00"));

		timeslot1.setDayOfWeek(DayOfWeek.MONDAY);
		timeslot2.setDayOfWeek(DayOfWeek.MONDAY);
		timeslot3.setDayOfWeek(DayOfWeek.MONDAY);
		timeslot4.setDayOfWeek(DayOfWeek.MONDAY);
		timeslot5.setDayOfWeek(DayOfWeek.MONDAY);
		timeslot6.setDayOfWeek(DayOfWeek.MONDAY);
		timeslot7.setDayOfWeek(DayOfWeek.MONDAY);
		timeslot8.setDayOfWeek(DayOfWeek.MONDAY);
		timeslot9.setDayOfWeek(DayOfWeek.MONDAY);
		timeslot10.setDayOfWeek(DayOfWeek.MONDAY);

		timeslot1.setId(1L);
		timeslot2.setId(2L);
		timeslot3.setId(3L);
		timeslot4.setId(4L);
		timeslot5.setId(5L);
		timeslot6.setId(6L);
		timeslot7.setId(7L);
		timeslot8.setId(8L);
		timeslot9.setId(9L);
		timeslot10.setId(10L);

		List<Timeslot> timeslots = new ArrayList<>();
		timeslots.add(timeslot1);
		timeslots.add(timeslot2);
		timeslots.add(timeslot3);
		timeslots.add(timeslot4);
		timeslots.add(timeslot5);
		timeslots.add(timeslot6);
		timeslots.add(timeslot7);
		timeslots.add(timeslot8);
		timeslots.add(timeslot9);
		timeslots.add(timeslot10);

		List<Timeslot> newTimeslots = timeslotService.getTimeslots(timeslots, 5);

		Assert.isTrue(newTimeslots.size() == 1,
				"The size of the new timeslots should be 1");

		Assert.isTrue(newTimeslots.get(0).getStartTime().equals(LocalTime.parse("08:00")),
				"The start time of the new timeslot should be 08:00");

		Assert.isTrue(newTimeslots.get(0).getEndTime().equals(LocalTime.parse("13:00")),
				"The end time of the new timeslot should be 13:00");

		Assert.isTrue(newTimeslots.get(0).getDayOfWeek().equals(DayOfWeek.MONDAY),
				"The day of the week of the new timeslot should be MONDAY");

		Assert.isTrue(newTimeslots.get(0).getId() == 1L,
				"The id of the new timeslot should be 1");

		timeslots.add(timeslot1);
		timeslots.add(timeslot2);
		timeslots.add(timeslot3);
		timeslots.add(timeslot4);
		timeslots.add(timeslot5);
		timeslots.add(timeslot6);
		timeslots.add(timeslot7);
		timeslots.add(timeslot8);
		timeslots.add(timeslot9);
		timeslots.add(timeslot10);

		newTimeslots = timeslotService.getTimeslots(timeslots, 2.5);

		Assert.isTrue(newTimeslots.size() == 2,
				"The size of the new timeslots should be 2");

		Assert.isTrue(newTimeslots.get(0).getId() == 1L,
				"The id of the first timeslot should be 1");

		Assert.isTrue(newTimeslots.get(0).getStartTime().equals(LocalTime.parse("08:00")),
				"The start time of the first timeslot should be 08:00");

		Assert.isTrue(newTimeslots.get(0).getEndTime().equals(LocalTime.parse("10:30")),
				"The end time of the first timeslot should be 10:30");

		Assert.isTrue(newTimeslots.get(0).getDayOfWeek().equals(DayOfWeek.MONDAY),
				"The day of the week of the first timeslot should be MONDAY");

		Assert.isTrue(newTimeslots.get(1).getId() == 6L,
				"The id of the second timeslot should be 6");

		Assert.isTrue(newTimeslots.get(1).getStartTime().equals(LocalTime.parse("10:30")),
				"The start time of the second timeslot should be 10:30");

		Assert.isTrue(newTimeslots.get(1).getEndTime().equals(LocalTime.parse("13:00")),
				"The end time of the second timeslot should be 13:00");

		Assert.isTrue(newTimeslots.get(1).getDayOfWeek().equals(DayOfWeek.MONDAY),
				"The day of the week of the second timeslot should be MONDAY");
	}

	@Test
	void testSplitTimeslot(){
		Timeslot timeslot = new Timeslot();
		timeslot.setStartTime(LocalTime.parse("08:00"));
		timeslot.setEndTime(LocalTime.parse("13:00"));
		timeslot.setDayOfWeek(DayOfWeek.of(1));
		timeslot.setId(1L);

		List<Timeslot> timeslots = timeslotService.splitTimeslot(timeslot, 5);

		Assert.isTrue(timeslots.size() == 2,
				"The timeslot was split into 2 timeslots");

		Assert.isTrue(timeslots.get(0).getId() == 1L,
				"The first timeslot has the id of 1");

		Assert.isTrue(timeslots.get(0).getStartTime().equals(LocalTime.parse("08:00")),
				"The first timeslot has the start time of 08:00");

		Assert.isTrue(timeslots.get(0).getEndTime().equals(LocalTime.parse("10:30")),
				"The first timeslot has the end time of 10:30");

		Assert.isTrue(timeslots.get(0).getDayOfWeek().equals(DayOfWeek.of(1)),
				"The first timeslot has the day of week of 1");

		Assert.isTrue(timeslots.get(1).getId() == 6L,
				"The second timeslot has the id of 6");

		Assert.isTrue(timeslots.get(1).getStartTime().equals(LocalTime.parse("10:30")),
				"The second timeslot has the start time of 10:30");

		Assert.isTrue(timeslots.get(1).getEndTime().equals(LocalTime.parse("13:00")),
				"The second timeslot has the end time of 13:00");

		Assert.isTrue(timeslots.get(1).getDayOfWeek().equals(DayOfWeek.of(1)),
				"The second timeslot has the day of week of 1");
	}
}
