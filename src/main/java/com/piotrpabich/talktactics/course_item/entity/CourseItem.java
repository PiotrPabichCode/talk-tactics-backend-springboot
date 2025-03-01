package com.piotrpabich.talktactics.course_item.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.CourseConstants;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "course_items")
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
public class CourseItem extends CommonEntity {

    private UUID uuid = UUID.randomUUID();

    private String word;

    private String phonetic;

    private String partOfSpeech;

    private String audio;

    @JsonIgnoreProperties("course")
    @OneToMany(mappedBy = "courseItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Meaning> meanings = new ArrayList<>();

    @JsonIgnoreProperties({"courseItems", "userCourses"})
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "courseItem")
    private List<UserCourseItem> userCourseItems;

    public int getPoints() {
        return switch (this.course.getLevel()) {
            case BEGINNER -> CourseConstants.BEGINNER_POINTS;
            case INTERMEDIATE -> CourseConstants.INTERMEDIATE_POINTS;
            case ADVANCED -> CourseConstants.ADVANCED_POINTS;
        };
    }
}
