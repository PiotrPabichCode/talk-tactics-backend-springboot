package com.example.talktactics.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "meanings")
public class Meaning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 800)
    private String definition;
    @Column(length = 800)
    private String example;

    @JsonIgnoreProperties("meanings")
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_item_id")
    private CourseItem courseItem;
}
