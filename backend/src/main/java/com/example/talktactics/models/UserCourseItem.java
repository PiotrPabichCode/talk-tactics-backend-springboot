package com.example.talktactics.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "user_course_items")
public class UserCourseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "course_item_id")
    private CourseItem courseItem;
    private boolean isLearned;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_course_id")
    private UserCourse userCourse;
}
