package com.piotrpabich.talktactics.course.participant;

import com.piotrpabich.talktactics.auth.AuthenticationService;
import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantDto;
import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantQueryCriteria;
import com.piotrpabich.talktactics.course.participant.dto.CourseParticipantRequest;
import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.COURSE_PARTICIPANTS_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + COURSE_PARTICIPANTS_PATH)
@Tag(name = "UserCourseController")
public class CourseParticipantController {

    private final CourseParticipantFacade courseParticipantFacade;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<Page<CourseParticipantDto>> queryCourseParticipants(
            final CourseParticipantQueryCriteria criteria,
            final Pageable pageable,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(courseParticipantFacade.queryAll(criteria, pageable, requester));
    }

    @GetMapping("/{courseParticipantUuid}")
    public ResponseEntity<CourseParticipant> getParticipantByUuid(
            @PathVariable final UUID courseParticipantUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(courseParticipantFacade.getCourseParticipantByUuid(courseParticipantUuid, requester));
    }

    @PutMapping
    public ResponseEntity<Void> assignCourseToUser(
            @RequestBody @Valid final CourseParticipantRequest addRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseParticipantFacade.assignCourseToUser(addRequest, requester);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeUserFromCourse(
            @RequestBody @Valid final CourseParticipantRequest deleteRequest,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseParticipantFacade.removeCourseFromUser(deleteRequest, requester);
        return ResponseEntity.noContent().build();
    }
}