package com.piotrpabich.talktactics.course.participant.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.participant.word.entity.CourseParticipantWord;
import com.piotrpabich.talktactics.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "course_participants")
@Getter
@Setter
@RequiredArgsConstructor
public class CourseParticipant extends CommonEntity {

    private UUID uuid = UUID.randomUUID();
    private Double progress = 0.0;
    private Boolean completed = false;
    private Integer points = 0;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    private boolean isFinished = false;
    @OneToMany(mappedBy = "courseParticipant", orphanRemoval = true)
    private List<CourseParticipantWord> courseParticipantWords = new ArrayList<>();

    public CourseParticipant(final User user, final Course course) {
        this.user = user;
        this.course = course;
    }

    public void addPoints(final int points) {
        this.points += points;
        this.getUser().addPoints(points);
    }
}
