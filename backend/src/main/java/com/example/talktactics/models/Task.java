package com.example.talktactics.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String word;
    private String partOfSpeech;
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
