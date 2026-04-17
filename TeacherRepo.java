package com.example.demo;

import org.springframework.stereotype.Repository;

@Repository
public class TeacherRepo {

    public TeacherRepo() {
        System.out.println("TeacherRepo constructor called");
    }

    public String getTeacher() {
        return "Mr. Kumar";
    }
}
