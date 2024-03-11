package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LessonUnitMapper {
    private final LessonRepository lessonRepository;

    @Autowired
    public LessonUnitMapper(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

//    o que eu quero?
//    quero pegar as lessons e criar lessonUnits
//    - a sala e timeslot devem estar zerados
//    - a criação deve ser feita com a lógica de blocos
//    -- ou seja, a propriedade size conterá o tamanho do bloco

    public List<LessonUnit> mapToBlocks(){
        return null;
    }

    public List<LessonUnit> getSize5(){
        return null;
    }

    public List<LessonUnit> getSize2Dot5(){
        return null;
    }

    public List<LessonUnit> getSize1(){
        return null;
    }

    public List<LessonUnit> getSize0Dot5(){
        return null;
    }
}
