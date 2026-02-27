package com.example.feeportal.controller;

import com.example.feeportal.model.Student;
import com.example.feeportal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Add student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    // Delete student
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    // Make payment
    @PutMapping("/pay/{id}")
    public Student makePayment(@PathVariable Long id, @RequestBody Map<String, Double> payment) {
        Double amount = payment.get("amount");
        return studentService.makePayment(id, amount);
    }

    // Get dashboard stats
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return studentService.getStats();
    }
}