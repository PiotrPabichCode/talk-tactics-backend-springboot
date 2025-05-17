package com.piotrpabich.talktactics.course.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.CourseConstants;
import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import com.piotrpabich.talktactics.course.word.entity.CourseWord;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "courses")
public class Course extends CommonEntity {

    private String title;

    private UUID uuid = UUID.randomUUID();

    @Column(length = 800)
    private String description;

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<CourseWord> courseWords = new ArrayList<>();

    private Integer quantity = 0;

    @OneToMany(mappedBy = "course", orphanRemoval = true)
    private List<CourseParticipant> courseParticipants = new ArrayList<>();

    @PreUpdate
    public void beforeUpdate() {
        if (this.getCourseWords() != null) {
            this.setQuantity(this.getCourseWords().size());
        }
    }

    public int getPoints() {
        return switch (this.level) {
            case BEGINNER -> CourseConstants.BEGINNER_COMPLETED_POINTS;
            case INTERMEDIATE -> CourseConstants.INTERMEDIATE_COMPLETED_POINTS;
            case ADVANCED -> CourseConstants.ADVANCED_COMPLETED_POINTS;
        };
    }
}
