package com.bot.service.impl;

import com.bot.model.Course;
import com.bot.service.CourseService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements CourseService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (InputStream coursesStream = getClass()
                .getClassLoader()
                .getResourceAsStream("courses.json")) {
            JsonNode rootNode = mapper.readTree(coursesStream);
            if (rootNode.hasNonNull("courses")) {
                JsonNode courseNode = rootNode.get("courses");
                courses.addAll(List.of(mapper.convertValue(courseNode, Course[].class)));
            }
        }
        return courses;
    }

}
