package com.example.talktactics.entity;

import com.example.talktactics.common.CommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "courses")
public class Course extends CommonEntity {
    private String name;
    @Column(length = 800)
    private String description;
    private CourseLevel level;

    @JsonIgnore
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<CourseItem> courseItems;

    @JsonIgnore
    @OneToMany(mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserCourse> userCourses;
}