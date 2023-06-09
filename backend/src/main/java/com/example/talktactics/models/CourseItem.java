package com.example.talktactics.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "course_items")
public class CourseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String word;
    private String phonetic;
    private String partOfSpeech;

    @OneToMany(mappedBy = "courseItem")
    private List<Meaning> meanings;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
