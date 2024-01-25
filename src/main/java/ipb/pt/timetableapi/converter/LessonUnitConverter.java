package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.LessonUnitDto;
import ipb.pt.timetableapi.model.Classroom;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.model.Timeslot;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LessonUnitConverter {
    public LessonUnitDto toDto(LessonUnit lessonUnit) {
        LessonUnitDto lessonUnitDto = new LessonUnitDto();
        BeanUtils.copyProperties(lessonUnit, lessonUnitDto);
        lessonUnitDto.setLessonId(lessonUnit.getLesson().getId());

        if (lessonUnit.getTimeslot() != null) {
            lessonUnitDto.setTimeslotId(lessonUnit.getTimeslot().getId());
        }

        if (lessonUnit.getClassroom() != null) {
            lessonUnitDto.setClassroomId(lessonUnit.getClassroom().getId());
        }

        return lessonUnitDto;
    }

    public List<LessonUnitDto> toDto(List<LessonUnit> lessonUnits) {
        List<LessonUnitDto> lessonUnitDtos = new ArrayList<>();
        for (LessonUnit lessonUnit : lessonUnits) {
            lessonUnitDtos.add(toDto(lessonUnit));
        }

        return lessonUnitDtos;
    }

    public LessonUnit toModel(LessonUnitDto lessonUnitDto) {
        LessonUnit lessonUnit = new LessonUnit();
        BeanUtils.copyProperties(lessonUnitDto, lessonUnit);

        Lesson lesson = new Lesson();
        lesson.setId(lessonUnitDto.getLessonId());
        lessonUnit.setLesson(lesson);

        if (lessonUnitDto.getTimeslotId() != null) {
            Timeslot timeslot = new Timeslot();
            timeslot.setId(lessonUnitDto.getTimeslotId());
            lessonUnit.setTimeslot(timeslot);
        }

        if (lessonUnitDto.getClassroomId() != null) {
            Classroom classroom = new Classroom();
            classroom.setId(lessonUnitDto.getClassroomId());
            lessonUnit.setClassroom(classroom);
        }

        return lessonUnit;
    }

    public List<LessonUnit> toModel(List<LessonUnitDto> lessonUnitDtos) {
        List<LessonUnit> lessonUnits = new ArrayList<>();
        for (LessonUnitDto lessonUnitDto : lessonUnitDtos) {
            lessonUnits.add(toModel(lessonUnitDto));
        }

        return lessonUnits;
    }
}
