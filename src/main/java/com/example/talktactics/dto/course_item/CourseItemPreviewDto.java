package com.example.talktactics.dto.course_item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseItemPreviewDto {
    long id;
    String word;
    @JsonProperty("part_of_speech")
    String partOfSpeech;
    String phonetic;
    @JsonProperty("course_name")
    String courseName;
}
