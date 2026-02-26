package com.example.feeportal.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String rollNo;
    private String studentClass;
    private Double totalFees;
    private Double paidFees = 0.0;
    private LocalDate lastPaymentDate;
    private String status = "DUE"; // DUE, PARTIAL, PAID

    // Getters and Setters (Right click → Generate)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getStudentClass() { return studentClass; }
    public void setStudentClass(String studentClass) { this.studentClass = studentClass; }

    public Double getTotalFees() { return totalFees; }
    public void setTotalFees(Double totalFees) { this.totalFees = totalFees; }

    public Double getPaidFees() { return paidFees; }
    public void setPaidFees(Double paidFees) { this.paidFees = paidFees; }

    public LocalDate getLastPaymentDate() { return lastPaymentDate; }
    public void setLastPaymentDate(LocalDate lastPaymentDate) { this.lastPaymentDate = lastPaymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getDueFees() {
        return totalFees - paidFees;
    }
}