package com.example.talktactics.dto.course_item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseItemPreviewDto {
    int id;
    String word;
    @JsonProperty("part_of_speech")
    String partOfSpeech;
    String phonetic;
    @JsonProperty("course_name")
    String courseName;
}
