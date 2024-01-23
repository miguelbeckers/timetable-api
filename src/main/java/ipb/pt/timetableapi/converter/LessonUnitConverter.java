package ipb.pt.timetableapi.converter;

import ipb.pt.timetableapi.dto.LessonUnitDto;
import ipb.pt.timetableapi.model.Lesson;
import ipb.pt.timetableapi.model.LessonUnit;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LessonUnitConverter {
    public LessonUnitDto toDto(LessonUnit lessonUnit) {
        LessonUnitDto lessonUnitDto = new LessonUnitDto();
        BeanUtils.copyProperties(lessonUnit, lessonUnitDto);
        lessonUnitDto.setTimeslotId(lessonUnit.getTimeslot().getId());
        lessonUnitDto.setLessonId(lessonUnit.getLesson().getId());
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
