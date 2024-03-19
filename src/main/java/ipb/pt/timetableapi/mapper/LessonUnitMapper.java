package ipb.pt.timetableapi.mapper;

import ipb.pt.timetableapi.solver.BlockSizeConstant;
import ipb.pt.timetableapi.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class LessonUnitMapper {
    public List<LessonUnit> mapBlocksToUnits(List<LessonUnit> lessonBlocks) {
        List<LessonUnit> lessonUnits = new ArrayList<>();
        lessonBlocks.forEach(lessonBlock -> lessonUnits.addAll(splitABlockIntoUnits(lessonBlock)));
        return lessonUnits;
    }

    private List<LessonUnit> splitABlockIntoUnits(LessonUnit lessonBlock) {
        List<LessonUnit> lessonUnits = new ArrayList<>();

        double blockSize = lessonBlock.getBlockSize();
        int units = (int) (blockSize / BlockSizeConstant.SIZE_0_5);

        for (int i = 0; i < units; i++) {
            LessonUnit lessonUnit = new LessonUnit();
            lessonUnit.setId(lessonBlock.getId() + i);
            lessonUnit.setBlockSize(BlockSizeConstant.SIZE_0_5);
            lessonUnit.setLesson(lessonBlock.getLesson());
            lessonUnit.setIsPinned(lessonBlock.getIsPinned());
            lessonUnit.setClassroom(lessonBlock.getClassroom());
            lessonUnit.setTimeslot(lessonBlock.getTimeslot());

            lessonUnits.add(lessonUnit);
        }

        return lessonUnits;
    }

    public List<LessonUnit> mapUnitsToBlocks(List<LessonUnit> lessonUnits) {
        List<LessonUnit> lessonBlocks = new ArrayList<>();

        HashMap<Lesson, List<LessonUnit>> lessonUnitsMap = new HashMap<>();
        lessonUnits.forEach(lessonUnit -> groupLessonUnitsByLesson(lessonUnitsMap, lessonUnit));

        for (Lesson lesson : lessonUnitsMap.keySet()) {
            List<LessonUnit> lessonUnitsWithSameLesson = lessonUnitsMap.get(lesson);
            List<LessonUnit> lessonBlocksForLesson = mergeUnitsIntoBlocks(lessonUnitsWithSameLesson, lesson);
            lessonBlocks.addAll(lessonBlocksForLesson);
        }

        return lessonBlocks;
    }

    private void groupLessonUnitsByLesson(HashMap<Lesson, List<LessonUnit>> lessonBlocksMap, LessonUnit lessonBlock) {
        Lesson lesson = lessonBlock.getLesson();
        if (lessonBlocksMap.containsKey(lesson)) {
            List<LessonUnit> lessonBlocksForLesson = lessonBlocksMap.get(lesson);
            lessonBlocksForLesson.add(lessonBlock);
        } else {
            List<LessonUnit> lessonBlocksForLesson = new ArrayList<>();
            lessonBlocksForLesson.add(lessonBlock);
            lessonBlocksMap.put(lesson, lessonBlocksForLesson);
        }
    }

    private List<LessonUnit> mergeUnitsIntoBlocks(List<LessonUnit> lessonUnits, Lesson lesson) {
        double blockSize = (double) lesson.getHoursPerWeek() / lesson.getBlocks();
        return new ArrayList<>(getBlocks(lessonUnits, lesson, blockSize));
    }

    private List<LessonUnit> getBlocks(List<LessonUnit> lessonUnits, Lesson lesson, double blockSize) {
        List<LessonUnit> lessonBlocksForLesson = new ArrayList<>();
        int unitsPerBlock = (int) (blockSize / BlockSizeConstant.SIZE_0_5);

        for (int i = 0; i < lesson.getBlocks(); i++) {
            LessonUnit currentLessonBlock = lessonUnits.get(0);
            LessonUnit lessonBlock = new LessonUnit();
            lessonBlock.setLesson(lesson);
            lessonBlock.setBlockSize(blockSize);
            lessonBlock.setId(currentLessonBlock.getId());
            lessonBlock.setIsPinned(currentLessonBlock.getIsPinned());
            lessonBlock.setClassroom(currentLessonBlock.getClassroom());
            lessonBlock.setTimeslot(currentLessonBlock.getTimeslot());

            lessonBlocksForLesson.add(lessonBlock);
            lessonUnits.subList(0, unitsPerBlock).clear();
        }
        return lessonBlocksForLesson;
    }

    public List<LessonUnit> mapBlocksToBlocks(List<LessonUnit> lessonBlocks, double blocksSize) {
        List<LessonUnit> newLessonBlocks = new ArrayList<>();

        HashMap<Lesson, List<LessonUnit>> lessonBlocksMap = new HashMap<>();
        lessonBlocks.forEach(lessonBlock -> groupLessonUnitsByLesson(lessonBlocksMap, lessonBlock));

        for (Lesson lesson : lessonBlocksMap.keySet()) {
            List<LessonUnit> lessonBlocksForLesson = lessonBlocksMap.get(lesson);
            List<LessonUnit> lessonBlocksForLessonWithSize = getBlocks(lessonBlocksForLesson, lesson, blocksSize);
            newLessonBlocks.addAll(lessonBlocksForLessonWithSize);
        }

        return newLessonBlocks;
    }

    public List<LessonUnit> getLessonBlocksBySize(List<LessonUnit> lessonUnits, double currentSize, Double nextSize, Double firstSize) {
        List<LessonUnit> lessonBlocks = mapUnitsToBlocks(lessonUnits);
        List<LessonUnit> lessonBlocksOfTheCurrentSize = getLessonBlocksBySize(lessonBlocks, currentSize, nextSize);

        if (firstSize == currentSize) {
            return lessonBlocksOfTheCurrentSize;
        }

        List<LessonUnit> lessonBlocksOfThePreviousSize = getLessonBlocksBySize(lessonBlocks, firstSize, currentSize);
        List<LessonUnit> previousLessonBlocksSplitIntoTheCurrentSize = mapBlocksToBlocks(lessonBlocksOfThePreviousSize, currentSize);

        return new ArrayList<>() {{
            addAll(lessonBlocksOfTheCurrentSize);
            addAll(previousLessonBlocksSplitIntoTheCurrentSize);
        }};
    }

    private List<LessonUnit> getLessonBlocksBySize(List<LessonUnit> lessonBlocks, double currentSize, Double nextSize) {
        return lessonBlocks.stream().filter(lessonUnit -> lessonUnit.getBlockSize() <= currentSize
                && (nextSize == null || lessonUnit.getBlockSize() > nextSize)).toList();
    }
}
