package ipb.pt.timetableapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ipb.pt.timetableapi.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/data")
public class DataController {
    @Autowired
    private RestTemplate restTemplate;

    private static final String URL = "http://localhost:8081";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/load")
    public ResponseEntity<Object> load() throws JsonProcessingException {
        objectMapper.registerModule(new JavaTimeModule());

        ResponseEntity<String> classroomData = restTemplate.exchange(
                URL + "/classrooms", HttpMethod.GET, null, String.class);

        ResponseEntity<String> classroomResourceData = restTemplate.exchange(
                URL + "/classroom-resources", HttpMethod.GET, null, String.class);

        ResponseEntity<String> classroomTypeData = restTemplate.exchange(
                URL + "/classroom-types", HttpMethod.GET, null, String.class);

        ResponseEntity<String> courseData = restTemplate.exchange(
                URL + "/courses", HttpMethod.GET, null, String.class);

        ResponseEntity<String> departmentData = restTemplate.exchange(
                URL + "/departments", HttpMethod.GET, null, String.class);

        ResponseEntity<String> lessonData = restTemplate.exchange(
                URL + "/lessons", HttpMethod.GET, null, String.class);

        ResponseEntity<String> lessonResourceData = restTemplate.exchange(
                URL + "/lesson-resources", HttpMethod.GET, null, String.class);

        ResponseEntity<String> lessonUnitData = restTemplate.exchange(
                URL + "/lesson-units", HttpMethod.GET, null, String.class);

        ResponseEntity<String> periodData = restTemplate.exchange(
                URL + "/periods", HttpMethod.GET, null, String.class);

        ResponseEntity<String> professorData = restTemplate.exchange(
                URL + "/professors", HttpMethod.GET, null, String.class);

        ResponseEntity<String> resourceData = restTemplate.exchange(
                URL + "/resources", HttpMethod.GET, null, String.class);

        ResponseEntity<String> studentData = restTemplate.exchange(
                URL + "/students", HttpMethod.GET, null, String.class);

        ResponseEntity<String> subjectCourseData = restTemplate.exchange(
                URL + "/subject-courses", HttpMethod.GET, null, String.class);

        ResponseEntity<String> subjectData = restTemplate.exchange(
                URL + "/subjects", HttpMethod.GET, null, String.class);

        ResponseEntity<String> timeslotData = restTemplate.exchange(
                URL + "/timeslots", HttpMethod.GET, null, String.class);

        List<ClassroomDto> classroomDtos = objectMapper.readValue(classroomData.getBody(), new TypeReference<>() {
        });

        List<ClassroomResourceDto> classroomResourceDtos = objectMapper.readValue(classroomResourceData.getBody(), new TypeReference<>() {
        });

        List<ClassroomTypeDto> classroomTypeDtos = objectMapper.readValue(classroomTypeData.getBody(), new TypeReference<>() {
        });

        List<CourseDto> courseDtos = objectMapper.readValue(courseData.getBody(), new TypeReference<>() {
        });

        List<DepartmentDto> departmentDtos = objectMapper.readValue(departmentData.getBody(), new TypeReference<>() {
        });

        List<LessonDto> lessonDtos = objectMapper.readValue(lessonData.getBody(), new TypeReference<>() {
        });

        List<LessonResourceDto> lessonResourceDtos = objectMapper.readValue(lessonResourceData.getBody(), new TypeReference<>() {
        });

        List<LessonUnitDto> lessonUnitDtos = objectMapper.readValue(lessonUnitData.getBody(), new TypeReference<>() {
        });

        List<PeriodDto> periodDtos = objectMapper.readValue(periodData.getBody(), new TypeReference<>() {
        });

        List<ProfessorDto> professorDtos = objectMapper.readValue(professorData.getBody(), new TypeReference<>() {
        });

        List<ResourceDto> resourceDtos = objectMapper.readValue(resourceData.getBody(), new TypeReference<>() {
        });

        List<StudentDto> studentDtos = objectMapper.readValue(studentData.getBody(), new TypeReference<>() {
        });

        List<SubjectCourseDto> subjectCourseDtos = objectMapper.readValue(subjectCourseData.getBody(), new TypeReference<>() {
        });

        List<SubjectDto> subjectDtos = objectMapper.readValue(subjectData.getBody(), new TypeReference<>() {
        });

        List<TimeslotDto> timeslotDtos = objectMapper.readValue(timeslotData.getBody(), new TypeReference<>() {
        });

        return ResponseEntity.ok().build();
    }
}
