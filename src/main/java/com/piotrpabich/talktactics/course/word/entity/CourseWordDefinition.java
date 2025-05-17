package com.piotrpabich.talktactics.course.word.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course_word_definitions")
@Getter
@Setter
@RequiredArgsConstructor
public class CourseWordDefinition extends CommonEntity {

    @Column(length = 800)
    private String definition;

    @Column(length = 800)
    private String example;

    @ManyToOne
    @JoinColumn(name = "course_word_id")
    private CourseWord courseWord;
}
