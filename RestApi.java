package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollegeRestController {

    private final CourseService courseService;
    private final TeacherRepo teacherRepo;

    // ONE constructor → constructor injection
    public CollegeRestController(CourseService courseService,
                                 TeacherRepo teacherRepo) {
        this.courseService = courseService;
        this.teacherRepo = teacherRepo;
        System.out.println("CollegeRestController constructor called");
    }

    @GetMapping("/college")
    public String collegeInfo() {
        return "Course: " + courseService.getCourse()
                + ", Teacher: " + teacherRepo.getTeacher();
    }
}
