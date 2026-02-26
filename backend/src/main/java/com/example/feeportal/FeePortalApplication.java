package com.example.feeportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeePortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeePortalApplication.class, args);
        System.out.println("✅ Server running on http://localhost:8080");
    }
}