package com.piotrpabich.talktactics.course.word.dto;

import com.piotrpabich.talktactics.course.word.entity.CourseWordDefinition;

public record CourseWordDefinitionDto(
        String definition,
        String example
) {

    public static CourseWordDefinitionDto of(final CourseWordDefinition definition) {
        return new CourseWordDefinitionDto(definition.getDefinition(), definition.getExample());
    }
}
