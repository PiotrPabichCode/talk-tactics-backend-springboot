package com.example.talktactics.dto.course;

import com.example.talktactics.entity.CourseLevel;
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
    public long id;
    public String title;
    public CourseLevel level;
    public int quantity;

    public static CourseNavbarDto fromTuple(Tuple tuple) {
        return new CourseNavbarDto(
                tuple.get("id", Long.class),
                tuple.get("title", String.class),
                CourseLevel.fromShort(tuple.get("level", Short.class)),
                tuple.get("quantity", Integer.class)
        );
    }
}
