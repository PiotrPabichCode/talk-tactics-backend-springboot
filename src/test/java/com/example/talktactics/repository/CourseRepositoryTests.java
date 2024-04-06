package com.example.talktactics.repository;

import com.example.talktactics.dto.course.CoursePreviewProjection;
import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseLevel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Comparator;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class CourseRepositoryTests {
    @Autowired
    private CourseRepository courseRepository;

    private Course course;
    private List<Course> courseList;

    @BeforeEach
    public void init() {
        course = Course.builder()
                .id(1)
                .title("English for Beginners")
                .level(CourseLevel.BEGINNER)
                .description("A comprehensive course designed to help beginners learn English from scratch.")
                .build();
        courseList = List.of(Course.builder()
                        .id(1)
                        .title("English for Beginners")
                        .level(CourseLevel.BEGINNER)
                        .description("A comprehensive course designed to help beginners learn English from scratch.")
                        .build(),
                Course.builder()
                        .id(2)
                        .title("Intermediate English")
                        .level(CourseLevel.INTERMEDIATE)
                        .description("A course tailored to help learners improve their English language skills.")
                        .build(),
                Course.builder()
                        .id(3)
                        .title("Advanced English")
                        .level(CourseLevel.ADVANCED)
                        .description("An advanced course tailored to help learners master the intricacies of the English language.")
                        .build());
    }

    @Test
    public void CourseRepository_Save_ReturnsCourse() {
        Course savedCourse = courseRepository.save(course);

        Assertions.assertThat(savedCourse).isNotNull();
        Assertions.assertThat(savedCourse.getId()).isGreaterThan(0);
    }

    @Test
    public void CourseRepository_FindAll_ReturnsListOfCourses() {
        courseRepository.saveAll(courseList);
        List<Course> courses = courseRepository.findAll();

        Assertions.assertThat(courses).isNotNull();
        Assertions.assertThat(courses.size()).isEqualTo(3);
    }

    @Test
    public void CourseRepository_FindByLevel_ReturnsListOfCourses() {
        courseRepository.saveAll(courseList);
        List<Course> courses = courseRepository.findByLevelName(CourseLevel.BEGINNER.toString());

        Assertions.assertThat(courses).isNotNull();
        Assertions.assertThat(courses.size()).isEqualTo(1);
    }

    @Test
    public void CourseRepository_FindByTitle_ReturnsCourse() {
        courseRepository.saveAll(courseList);
        Course foundCourse = courseRepository.findByTitle("Intermediate English");

        Assertions.assertThat(foundCourse).isNotNull();
        Assertions.assertThat(foundCourse.getTitle()).isEqualTo("Intermediate English");
    }

    @Test
    public void CourseRepository_Delete_RemovesCourse() {
        Course savedCourse = courseRepository.save(course);
        courseRepository.delete(savedCourse);

        Assertions.assertThat(courseRepository.findById(savedCourse.getId())).isEmpty();
    }

    @Test
    public void CourseRepository_DeleteAll_RemovesAllCourses() {
        courseRepository.saveAll(courseList);
        courseRepository.deleteAll();

        Assertions.assertThat(courseRepository.findAll()).isEmpty();
    }

    @Test
    public void CourseRepository_FindCoursePreviews_ReturnsListOfCoursePreviewsOrderedById() {
        courseRepository.saveAll(courseList);
        List<CoursePreviewProjection> coursePreviews = courseRepository.findCoursePreviews();

        Assertions.assertThat(coursePreviews)
                .isNotNull()
                .hasSize(3)
                .isSortedAccordingTo(Comparator.comparingLong(CoursePreviewProjection::getId));
    }
}
