package com.bot.service;

import com.bot.model.Course;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    List<Course> getAllCourses() throws IOException;
}
