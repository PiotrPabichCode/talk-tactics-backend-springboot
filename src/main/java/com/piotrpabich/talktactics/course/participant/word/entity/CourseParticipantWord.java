package com.piotrpabich.talktactics.course.participant.word.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.word.entity.CourseWord;
import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "course_participant_words")
@Getter
@Setter
@RequiredArgsConstructor
public class CourseParticipantWord extends CommonEntity {

    private UUID uuid = UUID.randomUUID();

    public CourseParticipantWord(final CourseParticipant courseParticipant, final CourseWord courseWord) {
        this.courseParticipant = courseParticipant;
        this.courseWord = courseWord;
    }

    @ManyToOne
    @JoinColumn(name = "course_word_id")
    private CourseWord courseWord;

    private boolean isLearned = false;

    @ManyToOne
    @JoinColumn(name = "course_participant_id")
    private CourseParticipant courseParticipant;
}
