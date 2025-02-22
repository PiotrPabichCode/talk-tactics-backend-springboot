package com.piotrpabich.talktactics.course_item.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.piotrpabich.talktactics.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "meanings")
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
public class Meaning extends CommonEntity {

    @Column(length = 800)
    private String definition;

    @Column(length = 800)
    private String example;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_item_id")
    private CourseItem courseItem;
}
