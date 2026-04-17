package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class CourseService {

    public CourseService() {
        System.out.println("CourseService constructor called");
    }

    public String getCourse() {
        return "Spring Boot";
    }
}
