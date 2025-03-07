package com.piotrpabich.talktactics.course.participant;

import com.piotrpabich.talktactics.course.CourseService;
import com.piotrpabich.talktactics.user.UserService;
import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantDto;
import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantQueryCriteria;
import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantRequest;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;

@Service
@Log4j2
@RequiredArgsConstructor
public class CourseParticipantFacade {

    private final CourseParticipantService courseParticipantService;
    private final UserService userService;
    private final CourseService courseService;

    public Page<CourseParticipantDto> queryAll(
            final CourseParticipantQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        return courseParticipantService.queryAll(criteria, pageable, requester);
    }

    public CourseParticipant getCourseParticipantByUuid(final UUID uuid, final User requester) {
        return courseParticipantService.getCourseParticipantByUuid(uuid, requester);
    }

    @Transactional
    public void assignCourseToUser(final CourseParticipantRequest request, final User requester) {
        final var user = userService.getUserByUuid(request.userUuid());
        validateIfUserHimselfOrAdmin(requester, user);
        final var course = courseService.getCourseByUuid(request.courseUuid());
        courseParticipantService.assignCourseToUser(user, course);
    }

    public void removeCourseFromUser(final CourseParticipantRequest request, final User requester) {
        final var user = userService.getUserByUuid(request.userUuid());
        validateIfUserHimselfOrAdmin(requester, user);
        courseParticipantService.removeCourseFromUser(request);
    }
}
