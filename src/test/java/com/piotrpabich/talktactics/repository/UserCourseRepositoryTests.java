package com.piotrpabich.talktactics.repository;

import com.piotrpabich.talktactics.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class UserCourseRepositoryTests {
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private TestEntityManager testEntityManager;
    private List<Course> courseList;
    private User user;

    private UserCourse userCourse;
    private List<UserCourse> userCourseList;

    @BeforeEach
    public void init() {
        courseList = List.of(
                testEntityManager.persistFlushFind(Course.builder()
                        .title("English for Beginners")
                        .level(CourseLevel.BEGINNER)
                        .description("A comprehensive course designed to help beginners learn English from scratch.")
                        .build()),
                testEntityManager.persistFlushFind(Course.builder()
                        .title("Intermediate English")
                        .level(CourseLevel.INTERMEDIATE)
                        .description("A course tailored to help learners improve their English language skills.")
                        .build())
        );
        user = testEntityManager.persistFlushFind(User.builder()
                .email("waynerooney@email.com")
                .firstName("Wayne")
                .lastName("Rooney")
                .role(Role.USER)
                .username("wayne_rooney")
                .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                .build());

        userCourse = UserCourse.builder()
                .id(1L)
                .course(courseList.get(0))
                .user(user)
                .progress(37.0)
                .completed(false)
                .build();
        userCourseList = List.of(UserCourse.builder()
                        .course(courseList.get(0))
                        .user(user)
                        .progress(37.0)
                        .completed(false)
                        .build(),
                UserCourse.builder()
                        .course(courseList.get(1))
                        .user(user)
                        .progress(100)
                        .completed(true)
                        .build()
                );

        userCourseRepository.saveAll(userCourseList);
    }

    @Test
    public void UserCourseRepository_Save_ReturnsUserCourse() {
        UserCourse savedUserCourse = userCourseRepository.save(userCourse);

        Assertions.assertThat(savedUserCourse).isNotNull();
        Assertions.assertThat(savedUserCourse.getId()).isGreaterThan(0);
    }

    @Test
    public void UserCourseRepository_FindAll_ReturnsListOfUserCourses() {
        List<UserCourse> userCourses = userCourseRepository.findAll();

        Assertions.assertThat(userCourses).isNotNull();
        Assertions.assertThat(userCourses.size()).isEqualTo(2);
    }

    @Test
    public void UserCourseRepository_ExistsByCourseIdAndUserId_ReturnsTrue() {
        UserCourse testUserCourse = userCourseList.get(0);
        boolean exists = userCourseRepository.existsByCourseIdAndUserId(testUserCourse.getCourse().getId(), testUserCourse.getUser().getId());

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void UserCourseRepository_FindByCourseIdAndUserId_ReturnsUserCourse() {
        UserCourse testUserCourse = userCourseList.get(0);
        UserCourse result = userCourseRepository.findByCourseIdAndUserId(testUserCourse.getCourse().getId(),testUserCourse.getUser().getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(testUserCourse.getId());
    }

    @Test
    public void UserCourseRepository_Delete_RemovesUserCourse() {
        userCourseRepository.delete(userCourseList.get(0));

        List<UserCourse> userCourses = userCourseRepository.findAll();

        Assertions.assertThat(userCourses.size()).isEqualTo(1);
    }

    @Test
    public void UserCourseRepository_DeleteAll_RemovesAllUserCourses() {
        userCourseRepository.deleteAll();

        List<UserCourse> userCourses = userCourseRepository.findAll();
        Assertions.assertThat(userCourses).isEmpty();
    }
}
