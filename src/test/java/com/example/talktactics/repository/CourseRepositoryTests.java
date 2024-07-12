package com.example.talktactics.repository;

import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseLevel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

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
                .id(1L)
                .title("English for Beginners")
                .level(CourseLevel.BEGINNER)
                .description("A comprehensive course designed to help beginners learn English from scratch.")
                .build();
        courseList = List.of(Course.builder()
                        .id(1L)
                        .title("English for Beginners")
                        .level(CourseLevel.BEGINNER)
                        .description("A comprehensive course designed to help beginners learn English from scratch.")
                        .build(),
                Course.builder()
                        .id(2L)
                        .title("Intermediate English")
                        .level(CourseLevel.INTERMEDIATE)
                        .description("A course tailored to help learners improve their English language skills.")
                        .build(),
                Course.builder()
                        .id(3L)
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
}
