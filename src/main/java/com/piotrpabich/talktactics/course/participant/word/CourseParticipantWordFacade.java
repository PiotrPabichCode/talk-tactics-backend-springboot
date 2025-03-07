package com.piotrpabich.talktactics.course.participant.word;

import com.piotrpabich.talktactics.user.UserService;
import com.piotrpabich.talktactics.course.participant.word.dto.CourseParticipantWordDto;
import com.piotrpabich.talktactics.course.participant.word.dto.CourseParticipantWordQueryCriteria;
import com.piotrpabich.talktactics.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseParticipantWordFacade {

    private final UserService userService;
    private final CourseParticipantWordService courseParticipantWordService;

    Page<CourseParticipantWordDto> queryAll(
            final CourseParticipantWordQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        final var user = userService.getUserByUuid(requester.getUuid());
        validateIfUserHimselfOrAdmin(requester, user);
        return courseParticipantWordService.queryAll(criteria, pageable);
    }

    void learnCourseParticipantWord(final UUID userCourseItemUuid, final User requester) {
        courseParticipantWordService.learnCourseParticipantWord(userCourseItemUuid, requester);
    }
}
