package com.example.feeportal.controller;

import com.example.feeportal.model.Student;
import com.example.feeportal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Add student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        student.setStatus("DUE");
        return studentRepository.save(student);
    }

    // Delete student
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

    // Make payment
    @PutMapping("/pay/{id}")
    public Student makePayment(@PathVariable Long id, @RequestBody Map<String, Double> payment) {
        Student student = studentRepository.findById(id).get();
        Double amount = payment.get("amount");

        student.setPaidFees(student.getPaidFees() + amount);
        student.setLastPaymentDate(LocalDate.now());

        if (student.getPaidFees() >= student.getTotalFees()) {
            student.setStatus("PAID");
        } else {
            student.setStatus("PARTIAL");
        }

        return studentRepository.save(student);
    }

    // Get dashboard stats
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        List<Student> all = studentRepository.findAll();
        long total = all.size();
        long paid = studentRepository.findByStatus("PAID").size();
        long due = studentRepository.findByStatus("DUE").size();
        long partial = studentRepository.findByStatus("PARTIAL").size();

        Double totalCollected = all.stream()
                .mapToDouble(Student::getPaidFees)
                .sum();

        return Map.of(
                "totalStudents", total,
                "paid", paid,
                "due", due,
                "partial", partial,
                "totalCollected", totalCollected
        );
    }
}