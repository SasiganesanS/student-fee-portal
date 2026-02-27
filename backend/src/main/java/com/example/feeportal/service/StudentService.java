package com.example.feeportal.service;

import com.example.feeportal.model.Student;
import com.example.feeportal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Add student
    public Student addStudent(Student student) {
        student.setStatus("DUE");
        return studentRepository.save(student);
    }

    // Delete student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Make payment
    public Student makePayment(Long id, Double amount) {
        Student student = studentRepository.findById(id).get();

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