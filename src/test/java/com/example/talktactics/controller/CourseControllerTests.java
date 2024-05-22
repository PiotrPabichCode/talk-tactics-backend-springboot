package com.example.talktactics.controller;

import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.dto.course.CoursePreviewProjectionImpl;
import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseLevel;
import com.example.talktactics.exception.CourseRuntimeException;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private List<CoursePreviewProjection> previewProjections;

    @BeforeEach
    public void init() {
        course = Course.builder()
                .id(1)
                .title("English for Beginners")
                .level(CourseLevel.BEGINNER)
                .courseItems(List.of())
                .description("A comprehensive course designed to help beginners learn English from scratch.")
                .build();
        courseList = List.of(Course.builder()
                        .id(1)
                        .title("English for Beginners")
                        .level(CourseLevel.BEGINNER)
                        .courseItems(List.of())
                        .description("A comprehensive course designed to help beginners learn English from scratch.")
                        .build(),
                Course.builder()
                        .id(2)
                        .title("Intermediate English")
                        .level(CourseLevel.INTERMEDIATE)
                        .courseItems(List.of())
                        .description("A course tailored to help learners improve their English language skills.")
                        .build(),
                Course.builder()
                        .id(3)
                        .title("Advanced English")
                        .level(CourseLevel.ADVANCED)
                        .courseItems(List.of())
                        .description("An advanced course tailored to help learners master the intricacies of the English language.")
                        .build());

        previewProjections = Arrays.asList(
                new CoursePreviewProjectionImpl(courseList.get(0).getId(), courseList.get(0).getTitle(), courseList.get(0).getDescription(), courseList.get(0).getLevel(), courseList.get(0).getCourseItems().size()),
                new CoursePreviewProjectionImpl(courseList.get(1).getId(), courseList.get(1).getTitle(), courseList.get(1).getDescription(), courseList.get(1).getLevel(), courseList.get(1).getCourseItems().size()),
                new CoursePreviewProjectionImpl(courseList.get(2).getId(), courseList.get(2).getTitle(), courseList.get(2).getDescription(), courseList.get(2).getLevel(), courseList.get(2).getCourseItems().size())
        );
    }

    @Test
    public void CourseController_CreateCourse_ReturnsStatus201() throws Exception {
        MockHttpServletRequestBuilder request = post(BASE_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated());

        verify(courseService).create(any(Course.class));
    }

    @Test
    public void CourseController_CreateCourse_Status422() throws Exception {
        doThrow(new CourseRuntimeException("Course creation failed.")).when(courseService).create(any(Course.class));

        MockHttpServletRequestBuilder request = post(BASE_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result -> assertEquals("422 UNPROCESSABLE_ENTITY \"Course creation failed.\"", result.getResolvedException().getMessage()));

        verify(courseService).create(any(Course.class));
    }

    @Test
    public void CourseController_GetCourseById_ReturnsCourse_Status200() throws Exception {
        long courseId = 2;
        Course returnedCourse = courseList.get(1);
        given(courseService.getById(any(long.class))).willReturn(returnedCourse);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + courseId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(returnedCourse.getId()))
                .andExpect(jsonPath("$.title").value(returnedCourse.getTitle()));

        verify(courseService).getById(any(long.class));
    }

    @Test
    public void CourseController_GetCourseById_Status404() throws Exception {
        long courseId = 4;
        given(courseService.getById(any(long.class))).willThrow(new CourseRuntimeException("Course not found."));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + courseId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Course not found.\"", result.getResolvedException().getMessage()));

        verify(courseService).getById(any(long.class));
    }

    @Test
    public void CourseController_GetAllCoursesPreview_ReturnsListOfCourses_Status200() throws Exception {
        given(courseService.getPreviewList()).willReturn(previewProjections);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/all/preview")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].title").value(previewProjections.get(0).getTitle()))
                .andExpect(jsonPath("$[1].title").value(previewProjections.get(1).getTitle()))
                .andExpect(jsonPath("$[2].title").value(previewProjections.get(2).getTitle()));

        verify(courseService).getPreviewList();
    }

    @Test
    public void CourseController_GetAllCoursesPreview_Status404() throws Exception {
        given(courseService.getPreviewList()).willThrow(new CourseRuntimeException("Courses not found."));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/all/preview")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Courses not found.\"", result.getResolvedException().getMessage()));

        verify(courseService).getPreviewList();
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

        doNothing().when(courseService).update(any(long.class), any(Course.class));

        MockHttpServletRequestBuilder request = put(BASE_URL + "/id/" + courseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCourse));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(courseService).update(any(long.class), any(Course.class));
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

        doThrow(new CourseRuntimeException("Course updateCourse failed.")).when(courseService).update(any(long.class), any(Course.class));

        MockHttpServletRequestBuilder request = put(BASE_URL + "/id/" + courseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCourse));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Course updateCourse failed.\"", result.getResolvedException().getMessage()));

        verify(courseService).update(any(long.class), any(Course.class));
    }

    @Test
    public void CourseController_DeleteCourse_Status200() throws Exception {
        long courseId = 3;
        doNothing().when(courseService).delete(any(long.class));

        MockHttpServletRequestBuilder request = delete(BASE_URL + "/id/" + courseId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(courseService).delete(any(long.class));
    }

    @Test
    public void CourseController_DeleteCourse_Status404() throws Exception {
        long courseId = 4;
        doThrow(new CourseRuntimeException("Course not found.")).when(courseService).delete(any(long.class));

        MockHttpServletRequestBuilder request = delete(BASE_URL + "/id/" + courseId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Course not found.\"", result.getResolvedException().getMessage()));

        verify(courseService).delete(any(long.class));
    }
}
