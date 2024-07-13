package com.piotrpabich.talktactics.service;

import com.piotrpabich.talktactics.entity.Course;
import com.piotrpabich.talktactics.entity.CourseLevel;
import com.piotrpabich.talktactics.repository.CourseRepository;
import com.piotrpabich.talktactics.service.course.CourseMapper;
import com.piotrpabich.talktactics.service.course.CourseServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig
public class CourseServiceTests {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private Course updatedCourse;
    private List<Course> courseList;

    @BeforeEach
    public void init() {
        courseService = new CourseServiceImpl(courseRepository, courseMapper);
        course = Course.builder()
                .id(1L)
                .title("English for Beginners")
                .level(CourseLevel.BEGINNER)
                .courseItems(List.of())
                .description("A comprehensive course designed to help beginners learn English from scratch.")
                .build();
        updatedCourse = Course.builder()
                .id(1L)
                .title("Beginner's English")
                .level(CourseLevel.BEGINNER)
                .courseItems(List.of())
                .description("A comprehensive course designed to help beginners learn English from scratch.")
                .build();
        courseList = List.of(Course.builder()
                        .id(1L)
                        .title("English for Beginners")
                        .level(CourseLevel.BEGINNER)
                        .courseItems(List.of())
                        .quantity(0)
                        .description("A comprehensive course designed to help beginners learn English from scratch.")
                        .build(),
                Course.builder()
                        .id(2L)
                        .title("Intermediate English")
                        .level(CourseLevel.INTERMEDIATE)
                        .courseItems(List.of())
                        .quantity(0)
                        .description("A course tailored to help learners improve their English language skills.")
                        .build(),
                Course.builder()
                        .id(3L)
                        .title("Advanced English")
                        .level(CourseLevel.ADVANCED)
                        .courseItems(List.of())
                        .quantity(0)
                        .description("An advanced course tailored to help learners master the intricacies of the English language.")
                        .build());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void CourseService_CreateCourse_CallsSaveMethod() {
        Course course = new Course();
        courseService.create(course);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    public void CourseService_GetById_ReturnsCourse() {
        given(courseRepository.findById(anyLong())).willReturn(Optional.of(course));

        Course foundCourse = courseService.getById(1);

        Assertions.assertThat(foundCourse).isNotNull();
        Assertions.assertThat(foundCourse.getTitle()).isEqualTo(course.getTitle());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void CourseService_UpdateCourse_CallsSaveMethod() {
        given(courseRepository.findById(anyLong())).willReturn(Optional.of(course));
        courseService.update(updatedCourse);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void CourseService_DeleteCourses_ReturnsVoid() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        courseService.delete(ids);
        ids.forEach(id -> verify(courseRepository).deleteById(id));
    }
}
