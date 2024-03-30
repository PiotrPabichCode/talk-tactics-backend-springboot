package com.example.talktactics.entity;

import com.example.talktactics.dto.course_item.CourseItemPreviewDto;
import com.example.talktactics.common.CommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "course_items")
public class CourseItem extends CommonEntity {
    private String word;
    private String phonetic;
    @JsonProperty("part_of_speech")
    private String partOfSpeech;

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

    public CourseItemPreviewDto toDTO() {
        return new CourseItemPreviewDto(this.getId(), this.word, this.partOfSpeech, this.phonetic, this.course.getTitle());
    }
}
