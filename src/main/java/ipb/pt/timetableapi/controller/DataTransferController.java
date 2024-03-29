package ipb.pt.timetableapi.controller;

import ipb.pt.timetableapi.dto.*;
import ipb.pt.timetableapi.service.DataTransferService;
import ipb.pt.timetableapi.service.LessonUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/data")
public class DataTransferController {
    private final String baseUrl = "http://localhost:8081";
    private final RestTemplate restTemplate;
    private final DataTransferService dataTransferService;
    private final LessonUnitService lessonUnitService;

    @Autowired
    public DataTransferController(
            RestTemplate restTemplate,
            DataTransferService dataTransferService,
            LessonUnitService lessonUnitService
    ) {
        this.restTemplate = restTemplate;
        this.dataTransferService = dataTransferService;
        this.lessonUnitService = lessonUnitService;
    }

    @Async
    @GetMapping("/load-all")
    public CompletableFuture<ResponseEntity<Object>> loadAll() {
        CompletableFuture<SubjectDto[]> subjectResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/subjects", SubjectDto[].class).getBody());

        CompletableFuture<PeriodDto[]> periodResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/periods", PeriodDto[].class).getBody());

        CompletableFuture<ResourceDto[]> resourceResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/resources", ResourceDto[].class).getBody());

        CompletableFuture<DepartmentDto[]> departmentResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/departments", DepartmentDto[].class).getBody());

        CompletableFuture<SubjectTypeDto[]> subjectTypeResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/subject-types", SubjectTypeDto[].class).getBody());

        CompletableFuture<TimeslotDto[]> timeslotResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/timeslots", TimeslotDto[].class).getBody());

        CompletableFuture<ProfessorDto[]> professorResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/professors", ProfessorDto[].class).getBody());

        CompletableFuture<CourseDto[]> courseResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/courses", CourseDto[].class).getBody());

        CompletableFuture<ClassroomResourceDto[]> classroomResourceResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/classroom-resources", ClassroomResourceDto[].class).getBody());

        CompletableFuture<ClassroomTypeDto[]> classroomTypeResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/classroom-types", ClassroomTypeDto[].class).getBody());

        CompletableFuture<ClassroomDto[]> classroomResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/classrooms", ClassroomDto[].class).getBody());

        CompletableFuture<LessonResourceDto[]> lessonResourceResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/lesson-resources", LessonResourceDto[].class).getBody());

        CompletableFuture<SubjectCourseDto[]> subjectCourseResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/subject-courses", SubjectCourseDto[].class).getBody());

        CompletableFuture<StudentDto[]> studentResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/students", StudentDto[].class).getBody());

        CompletableFuture<LessonDto[]> lessonResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/lessons", LessonDto[].class).getBody());

        CompletableFuture<LessonUnitDto[]> lessonUnitResponse = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(
                baseUrl + "/lesson-units", LessonUnitDto[].class).getBody());

        CompletableFuture.allOf(
                subjectResponse,
                periodResponse,
                resourceResponse,
                departmentResponse,
                subjectTypeResponse,
                timeslotResponse,
                professorResponse,
                courseResponse,
                classroomResourceResponse,
                classroomTypeResponse,
                classroomResponse,
                lessonResourceResponse,
                subjectCourseResponse,
                studentResponse,
                lessonResponse,
                lessonUnitResponse
        ).join();

        dataTransferService.persistAll(
                Arrays.asList(subjectResponse.join()),
                Arrays.asList(periodResponse.join()),
                Arrays.asList(resourceResponse.join()),
                Arrays.asList(departmentResponse.join()),
                Arrays.asList(subjectTypeResponse.join()),
                Arrays.asList(timeslotResponse.join()),
                Arrays.asList(professorResponse.join()),
                Arrays.asList(courseResponse.join()),
                Arrays.asList(classroomResourceResponse.join()),
                Arrays.asList(classroomTypeResponse.join()),
                Arrays.asList(classroomResponse.join()),
                Arrays.asList(lessonResourceResponse.join()),
                Arrays.asList(subjectCourseResponse.join()),
                Arrays.asList(studentResponse.join()),
                Arrays.asList(lessonResponse.join()),
                Arrays.asList(lessonUnitResponse.join())
        );

        return CompletableFuture.completedFuture(ResponseEntity.ok().body("Data loaded successfully!"));
    }

    @PostMapping("/export-result")
    public ResponseEntity<Object> exportResult() {
        restTemplate.postForEntity(baseUrl + "/horarios", lessonUnitService.findAll(), Void.class);
        return ResponseEntity.ok().body("Result exported successfully!");
    }
}
