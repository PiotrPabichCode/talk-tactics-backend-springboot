package com.example.talktactics.controller;

import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseLevel;
import com.example.talktactics.exception.BadRequestException;
import com.example.talktactics.exception.EntityNotFoundException;
import com.example.talktactics.service.course.CourseService;
import com.example.talktactics.service.jwt.JwtService;
import com.example.talktactics.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CourseControllerTests {

    private static final String BASE_URL = "/api/v1/courses";

    @MockBean
    private CourseService courseService;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Course course;
    private List<Course> courseList;

    @BeforeEach
    public void init() {
        course = Course.builder()
                .id(1L)
                .title("English for Beginners")
                .level(CourseLevel.BEGINNER)
                .courseItems(List.of())
                .description("A comprehensive course designed to help beginners learn English from scratch.")
                .build();
        courseList = List.of(Course.builder()
                        .id(1L)
                        .title("English for Beginners")
                        .level(CourseLevel.BEGINNER)
                        .courseItems(List.of())
                        .description("A comprehensive course designed to help beginners learn English from scratch.")
                        .build(),
                Course.builder()
                        .id(2L)
                        .title("Intermediate English")
                        .level(CourseLevel.INTERMEDIATE)
                        .courseItems(List.of())
                        .description("A course tailored to help learners improve their English language skills.")
                        .build(),
                Course.builder()
                        .id(3L)
                        .title("Advanced English")
                        .level(CourseLevel.ADVANCED)
                        .courseItems(List.of())
                        .description("An advanced course tailored to help learners master the intricacies of the English language.")
                        .build());
    }

    @Test
    public void CourseController_CreateCourse_ReturnsStatus201() throws Exception {
        MockHttpServletRequestBuilder request = post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        verify(courseService).create(any(Course.class));
    }
    @Test
    public void CourseController_UpdateCourse_ReturnsStatus204() throws Exception {
        long courseId = 2;
        Course updatedCourse = Course.builder()
                .id(courseId)
                .title("English for Intermediate Learners")
                .level(CourseLevel.INTERMEDIATE)
                .courseItems(List.of())
                .description("A course tailored to help learners improve their English language skills.")
                .build();

        doNothing().when(courseService).update(any(Course.class));

        MockHttpServletRequestBuilder request = put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCourse));

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(courseService).update(any(Course.class));
    }

    @Test
    public void CourseController_UpdateCourse_Status400() throws Exception {
        long courseId = 2;
        Course updatedCourse = Course.builder()
                .id(courseId)
                .title("English for Intermediate Learners")
                .level(CourseLevel.INTERMEDIATE)
                .courseItems(List.of())
                .description("A course tailored to help learners improve their English language skills.")
                .build();

        doThrow(new BadRequestException("Course updateCourse failed.")).when(courseService).update(any(Course.class));

        MockHttpServletRequestBuilder request = put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCourse));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Course updateCourse failed.", result.getResolvedException().getMessage()));

        verify(courseService).update(any(Course.class));
    }

    @Test
    public void CourseController_DeleteCourses_Status200() throws Exception {
        Set<Long> ids = Set.of(3L);
        doNothing().when(courseService).delete(anySet());

        MockHttpServletRequestBuilder request = delete(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ids));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(courseService).delete(anySet());
    }
}
