package com.piotrpabich.talktactics.course.word.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.CourseConstants;
import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.course.participant.word.entity.CourseParticipantWord;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "course_words")
@Getter
@Setter
@RequiredArgsConstructor
public class CourseWord extends CommonEntity {

    private UUID uuid = UUID.randomUUID();

    private String word;

    private String phonetic;

    private String partOfSpeech;

    private String audio;

    @OneToMany(mappedBy = "courseWord",
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<CourseWordDefinition> definitions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "courseWord")
    private List<CourseParticipantWord> courseParticipantWords;

    public int getPoints() {
        return switch (this.course.getLevel()) {
            case BEGINNER -> CourseConstants.BEGINNER_POINTS;
            case INTERMEDIATE -> CourseConstants.INTERMEDIATE_POINTS;
            case ADVANCED -> CourseConstants.ADVANCED_POINTS;
        };
    }
}
