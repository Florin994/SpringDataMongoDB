package com.florin.SpringDataMongoDB;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/students")
@AllArgsConstructor
public class StudentController {
    private final StudentServices studentServices;
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public List<Student> fetchAllStudents() {
        return studentServices.getAllStudents();
    }

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        try {
            student.setCreated(LocalDateTime.parse(String.valueOf(LocalDateTime.now())));
            if (studentRepository.findStudentByEmail(student.getEmail()).isPresent()) {
                return new ResponseEntity<>("Error: email already exists ", HttpStatus.CONFLICT);
            }
            studentRepository.save(student);
            return new ResponseEntity<Student>(student, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSingleStudent(@PathVariable("id") String id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            return new ResponseEntity<>(studentOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: no student found with id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable("id") String id, @RequestBody Student student) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student studentToSave = studentOptional.get();
            studentToSave.setFirstName(student.getFirstName() != null ?
                    student.getFirstName() : studentToSave.getFirstName());
            student.setEmail(student.getEmail() != null ? student.getEmail() : studentToSave.getEmail());
            studentToSave.setGender(student.getGender() != null ? student.getGender() : studentToSave.getGender());
            studentToSave.setAddress(student.getAddress() != null ? student.getAddress() : studentToSave.getAddress());
            studentToSave.setFavoriteSubjects(student.getFavoriteSubjects() != null ?
                    student.getFavoriteSubjects() : studentToSave.getFavoriteSubjects());
            studentToSave.setTotalSpentInBooks(student.getTotalSpentInBooks() != null ?
                    student.getTotalSpentInBooks() : studentToSave.getTotalSpentInBooks());
            studentRepository.save(studentToSave);
            return new ResponseEntity<>(studentToSave, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error: no student found with id " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteStudentByID(@PathVariable("id") String id) {
        if (studentRepository.findById(id).isPresent()) {
            studentRepository.deleteById(id);
            return new ResponseEntity<>("Student with id " + id + " was successfully deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("Error: no student found with id " + id, HttpStatus.NOT_FOUND);

    }
}
