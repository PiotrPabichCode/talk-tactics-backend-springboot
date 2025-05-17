package com.piotrpabich.talktactics.course.participant;

import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import com.piotrpabich.talktactics.course.participant.word.entity.CourseParticipantWord;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class CourseParticipantMapper {

    List<CourseParticipantWord> convert(final CourseParticipant courseParticipant) {
        return courseParticipant.getCourse().getCourseWords().stream()
                .map(courseItem -> new CourseParticipantWord(courseParticipant, courseItem))
                .collect(toList());
    }
}
