package ipb.pt.timetableapi.optimizer.service;

import ipb.pt.timetableapi.optimizer.model.Student;
import ipb.pt.timetableapi.optimizer.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findAll(){
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id){
        return studentRepository.findById(id);
    }

    public Student create(Student student){
        return studentRepository.save(student);
    }

    public Student update(Student student){
        return studentRepository.save(student);
    }

    public void delete(Student student){
        studentRepository.delete(student);
    }
}
