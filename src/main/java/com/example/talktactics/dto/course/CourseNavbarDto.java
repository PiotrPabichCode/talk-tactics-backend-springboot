package com.example.talktactics.dto.course;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CourseNavbarDto {
    long id;
    String title;
    String level;
    int quantity;

    public static CourseNavbarDto fromTuple(Tuple tuple) {
        return new CourseNavbarDto(
                tuple.get("id", Long.class),
                tuple.get("title", String.class),
                tuple.get("level", String.class),
                tuple.get("quantity", Integer.class)
        );
    }

}
