package ipb.pt.timetableapi;

import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timeslot;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalTime;

import static ipb.pt.timetableapi.solver.TimeTableConstraintProvider.checkIfTheLessonsAreOutOfTheBlock;

@SpringBootTest
class TimetableApiApplicationTests {
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
}
