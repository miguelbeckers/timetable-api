package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.converter.StudentConverter;
import ipb.pt.timetableapi.dto.StudentDto;
import ipb.pt.timetableapi.model.Student;
import ipb.pt.timetableapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentConverter studentConverter;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentConverter studentConverter) {
        this.studentRepository = studentRepository;
        this.studentConverter = studentConverter;
    }

    public List<StudentDto> findAll() {
        return studentConverter.toDto(studentRepository.findAll());
    }

    public StudentDto findById(Long id) {
        return studentConverter.toDto(studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found")));
    }

    public StudentDto create(StudentDto studentDto) {
        Student student = studentConverter.toModel(studentDto);
        return studentConverter.toDto(studentRepository.save(student));
    }

    public StudentDto update(StudentDto studentDto, Long id) {
        studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        Student student = studentConverter.toModel(studentDto);
        return studentConverter.toDto(studentRepository.save(student));
    }

    public void delete(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        studentRepository.delete(student);
    }

    public void deleteAll() {
        studentRepository.deleteAll();
    }

    public List<StudentDto> saveAll(List<StudentDto> studentDtos) {
        List<Student> students = studentConverter.toModel(studentDtos);
        return studentConverter.toDto(studentRepository.saveAll(students));
    }
}
