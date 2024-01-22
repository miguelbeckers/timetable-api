package ipb.pt.timetableapi.service;

import ipb.pt.timetableapi.dto.StudentDto;
import ipb.pt.timetableapi.model.Student;
import ipb.pt.timetableapi.repository.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public Student create(StudentDto studentDto) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDto, student);
        return studentRepository.save(student);
    }

    public Student update(StudentDto studentDto, Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        BeanUtils.copyProperties(studentDto, student);
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        studentRepository.delete(student);
    }

    public void createMany(List<StudentDto> studentDtos) {
        for (StudentDto studentDto : studentDtos) {
            Student student = new Student();
            BeanUtils.copyProperties(studentDto, student);
            studentRepository.save(student);
        }
    }
}
