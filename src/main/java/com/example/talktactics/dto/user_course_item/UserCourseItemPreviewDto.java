package com.example.talktactics.dto.user_course_item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class UserCourseItemPreviewDto {
    long id;
    @JsonProperty("course_item_id")
    long courseItemId;
    String word;
    @JsonProperty("part_of_speech")
    String partOfSpeech;
    String phonetic;
    boolean learned;
}
