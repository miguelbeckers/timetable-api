package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.LessonUnitConverter;
import ipb.pt.timetableapi.dto.LessonUnitDto;
import ipb.pt.timetableapi.mapper.LessonUnitMapper;
import ipb.pt.timetableapi.model.LessonUnit;
import ipb.pt.timetableapi.repository.LessonUnitRepository;
import ipb.pt.timetableapi.solver.SizeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonUnitService {
    private final LessonUnitRepository lessonUnitRepository;
    private final LessonUnitConverter lessonUnitConverter;
    private final LessonUnitMapper lessonUnitMapper;

    @Autowired
    public LessonUnitService(LessonUnitRepository lessonUnitRepository,
                             LessonUnitConverter lessonUnitConverter,
                             LessonUnitMapper lessonUnitMapper
    ) {
        this.lessonUnitRepository = lessonUnitRepository;
        this.lessonUnitConverter = lessonUnitConverter;
        this.lessonUnitMapper = lessonUnitMapper;
    }

    public List<LessonUnitDto> findAll() {
        return lessonUnitConverter.toDto(lessonUnitRepository.findAll());
    }

    public LessonUnitDto findById(Long id) {
        return lessonUnitConverter.toDto(lessonUnitRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found")));
    }

    public LessonUnitDto create(LessonUnitDto lessonUnitDto) {
        LessonUnit lessonUnit = lessonUnitConverter.toModel(lessonUnitDto);
        return lessonUnitConverter.toDto(lessonUnitRepository.save(lessonUnit));
    }

    public LessonUnitDto update(LessonUnitDto lessonUnitDto, Long id) {
        lessonUnitRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found"));

        LessonUnit lessonUnit = lessonUnitConverter.toModel(lessonUnitDto);
        return lessonUnitConverter.toDto(lessonUnitRepository.save(lessonUnit));
    }

    public LessonUnit update(LessonUnit lessonUnit) {
        lessonUnitRepository.findById(lessonUnit.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found"));

        return lessonUnitRepository.save(lessonUnit);
    }

    public void delete(Long id) {
        LessonUnit lessonUnit = lessonUnitRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LessonUnit not found"));

        lessonUnitRepository.delete(lessonUnit);
    }

    public void deleteAll() {
        lessonUnitRepository.deleteAll();
    }

    public List<LessonUnitDto> saveAll(List<LessonUnitDto> lessonUnitDtos) {
        List<LessonUnit> lessonUnits = lessonUnitConverter.toModel(lessonUnitDtos);
        return lessonUnitConverter.toDto(lessonUnitRepository.saveAll(lessonUnits));
    }

    public List<LessonUnitDto> resetAll() {
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();

        lessonUnits.forEach(lessonUnit -> {
            lessonUnit.setTimeslot(null);
            lessonUnit.setClassroom(null);
            lessonUnit.setIsPinned(false);
        });

        return lessonUnitConverter.toDto(lessonUnitRepository.saveAll(lessonUnits));
    }

    public List<LessonUnit> getLessonBlocksBySize(double size, Double nextSize, Double firstSize) {
        List<LessonUnit> lessonUnits = lessonUnitRepository.findAll();

        if (size == SizeConstant.SIZE_0_5) {
            return lessonUnits;
        }

        List<LessonUnit> lessonBlocks = lessonUnitMapper.mapUnitsToBlocks(lessonUnits);
        List<LessonUnit> lessonBlocksOfTheCurrentSize = getLessonBlocksBySize(lessonBlocks, size, nextSize);

        if (firstSize == size) {
            return lessonBlocksOfTheCurrentSize;
        }

        List<LessonUnit> lessonBlocksOfThePreviousSize = getLessonBlocksBySize(lessonBlocks, firstSize, size);
        List<LessonUnit> previousLessonBlocksSplitIntoTheCurrentSize = lessonUnitMapper.mapBlocksToBlocks(lessonBlocksOfThePreviousSize, size);

        return new ArrayList<>() {{
            addAll(lessonBlocksOfTheCurrentSize);
            addAll(previousLessonBlocksSplitIntoTheCurrentSize);
        }};
    }

    private List<LessonUnit> getLessonBlocksBySize(List<LessonUnit> lessonBlocks, double size, Double nextSize) {
        return lessonBlocks.stream().filter(lessonUnit -> lessonUnit.getBlockSize() <= size
                && (nextSize == null || lessonUnit.getBlockSize() > nextSize)).toList();
    }

    public List<LessonUnit> divideLessonBlocksIntoUnits(List<LessonUnit> lessonBlocks) {
        return lessonUnitMapper.mapBlocksToUnits(lessonBlocks);
    }
}
