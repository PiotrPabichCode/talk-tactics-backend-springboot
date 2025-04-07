package com.piotrpabich.talktactics.course.participant.word.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import com.piotrpabich.talktactics.course.word.entity.CourseWord;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "course_participant_words")
@Getter
@Setter
@RequiredArgsConstructor
public class CourseParticipantWord extends CommonEntity {

    private UUID uuid = UUID.randomUUID();
    @ManyToOne
    @JoinColumn(name = "course_word_id")
    private CourseWord courseWord;
    private boolean isLearned = false;
    @ManyToOne
    @JoinColumn(name = "course_participant_id")
    private CourseParticipant courseParticipant;

    public CourseParticipantWord(final CourseParticipant courseParticipant, final CourseWord courseWord) {
        this.courseParticipant = courseParticipant;
        this.courseWord = courseWord;
    }

    public void learnWord() {
        isLearned = true;
        this.getCourseParticipant().addPoints(courseWord.getPoints());
    }
}
