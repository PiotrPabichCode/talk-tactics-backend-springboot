package com.piotrpabich.talktactics.course_item.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.CourseConstants;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.entity.CourseLevel;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "course_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class CourseItem extends CommonEntity {
    private String word;
    private String phonetic;
    @JsonProperty("part_of_speech")
    private String partOfSpeech;
    @Nullable
    private String audio;
    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @JsonIgnoreProperties("course")
    @OneToMany(mappedBy = "courseItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Meaning> meanings;

    @JsonIgnoreProperties({"courseItems", "userCourses"})
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_course_item_id")
    private UserCourseItem userCourseItem;

    public int getPoints() {
        return switch (this.level) {
            case BEGINNER -> CourseConstants.BEGINNER_POINTS;
            case INTERMEDIATE -> CourseConstants.INTERMEDIATE_POINTS;
            case ADVANCED -> CourseConstants.ADVANCED_POINTS;
        };
    }
}
