package com.example.talktactics.service;

import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.dto.course.CoursePreviewProjectionImpl;
import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseLevel;
import com.example.talktactics.repository.CourseRepository;
import com.example.talktactics.service.course.CourseServiceImpl;
import com.example.talktactics.service.user.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringJUnitConfig
public class CourseServiceTests {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private Course updatedCourse;
    private List<Course> courseList;
    private List<CoursePreviewProjection> previewProjections;

    @BeforeEach
    public void init() {
        courseService = new CourseServiceImpl(courseRepository, userService);
        course = Course.builder()
                .id(1)
                .title("English for Beginners")
                .level(CourseLevel.BEGINNER)
                .courseItems(List.of())
                .description("A comprehensive course designed to help beginners learn English from scratch.")
                .build();
        updatedCourse = Course.builder()
                .id(1)
                .title("Beginner's English")
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
    @WithMockUser(authorities = {"ADMIN"})
    public void CourseService_CreateCourse_ReturnsCourse() {
        given(courseRepository.save(any(Course.class))).willReturn(course);

        Course createdCourse = courseService.create(course);

        Assertions.assertThat(createdCourse).isNotNull();
        Assertions.assertThat(createdCourse.getTitle()).isEqualTo(course.getTitle());
    }

    @Test
    public void CourseService_GetById_ReturnsCourse() {
        given(courseRepository.findById(any(long.class))).willReturn(Optional.of(course));

        Course foundCourse = courseService.getById(1);

        Assertions.assertThat(foundCourse).isNotNull();
        Assertions.assertThat(foundCourse.getTitle()).isEqualTo(course.getTitle());
    }

    @Test
    public void CourseService_GetPreviewList_ReturnsListOfCoursePreviewProjections() {
        given(courseRepository.findCoursePreviews()).willReturn(previewProjections);

        List<CoursePreviewProjection> coursePreviewProjections = courseService.getPreviewList();

        Assertions.assertThat(coursePreviewProjections).isNotNull();
        Assertions.assertThat(coursePreviewProjections.size()).isEqualTo(previewProjections.size());
        Assertions.assertThat(coursePreviewProjections.get(0).getTitle()).isEqualTo(previewProjections.get(0).getTitle());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void CourseService_UpdateCourse_ReturnsCourse() {
        given(courseRepository.findById(any(long.class))).willReturn(Optional.of(course));
        given(courseRepository.save(any(Course.class))).willReturn(updatedCourse);

        Course newCourse = courseService.update(1, updatedCourse);

        Assertions.assertThat(newCourse).isNotNull();
        Assertions.assertThat(newCourse.getTitle()).isEqualTo(updatedCourse.getTitle());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void CourseService_DeleteCourse_ReturnsVoid() {
        given(courseRepository.existsById(any(long.class))).willReturn(true);

        courseService.delete(1);
    }
}
