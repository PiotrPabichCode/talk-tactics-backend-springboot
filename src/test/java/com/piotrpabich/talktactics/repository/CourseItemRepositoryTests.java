package com.piotrpabich.talktactics.repository;

import com.piotrpabich.talktactics.entity.Course;
import com.piotrpabich.talktactics.entity.CourseItem;
import com.piotrpabich.talktactics.entity.CourseLevel;
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
public class CourseItemRepositoryTests {

    @Autowired
    private CourseItemRepository courseItemRepository;

    @Autowired
    private TestEntityManager testEntityManager;
    private List<Course> courseList;

    private CourseItem courseItem;
    private List<CourseItem> courseItemList;

    @BeforeEach
    public void init() {
        courseItem = CourseItem.builder()
                .word("apple")
                .phonetic("ˈæp.əl")
                .partOfSpeech("noun")
                .meanings(List.of())
                .build();

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

        courseItemList = List.of(
                CourseItem.builder()
                        .id(1L)
                        .course(courseList.get(0))
                        .word("apple")
                        .phonetic("ˈæp.əl")
                        .partOfSpeech("noun")
                        .meanings(List.of())
                        .build(),
                CourseItem.builder()
                        .id(2L)
                        .course(courseList.get(1))
                        .word("banana")
                        .phonetic("bəˈnɑː.nə")
                        .partOfSpeech("noun")
                        .meanings(List.of())
                        .build()
        );

        // Save course items
        courseItemRepository.saveAll(courseItemList);
    }

    @Test
    public void CourseItemRepository_Save_ReturnsCourseItem() {
        CourseItem savedCourseItem = courseItemRepository.save(courseItem);

        Assertions.assertThat(savedCourseItem).isNotNull();
        Assertions.assertThat(savedCourseItem.getId()).isGreaterThan(0);
    }

    @Test
    public void CourseItemRepository_FindAll_ReturnsListOfCourseItems() {
        List<CourseItem> foundCourseItemList = courseItemRepository.findAll();

        Assertions.assertThat(foundCourseItemList).isNotNull();
        Assertions.assertThat(foundCourseItemList.size()).isEqualTo(2);
    }

    @Test
    public void CourseItemRepository_FindByCourseId_ReturnsMultipleCourseItemList() {
        List<CourseItem> foundCourseItemList = courseItemRepository.findByCourseId(courseList.get(0).getId());

        Assertions.assertThat(foundCourseItemList).isNotNull();
        Assertions.assertThat(foundCourseItemList.size()).isEqualTo(1);
    }

    @Test
    public void CourseItemRepository_FindByCourseId_ReturnsEmptyList() {
        List<CourseItem> foundCourseItemList = courseItemRepository.findByCourseId(99);

        Assertions.assertThat(foundCourseItemList).isNotNull();
        Assertions.assertThat(foundCourseItemList.size()).isEqualTo(0);
    }

    @Test
    public void CourseItemRepository_Delete_RemovesCourseItem() {
        CourseItem deletedCourseItem = courseItemList.get(0);
        courseItemRepository.delete(deletedCourseItem);

        Assertions.assertThat(courseItemRepository.findById(deletedCourseItem.getId())).isEmpty();
    }

    @Test
    public void CourseItemRepository_DeleteAll_RemovesAllCourseItems() {
        courseItemRepository.deleteAll();

        Assertions.assertThat(courseItemRepository.findAll()).isEmpty();
    }
}
