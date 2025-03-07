package com.piotrpabich.talktactics.course.participant.word;

import com.piotrpabich.talktactics.auth.AuthenticationService;
import com.piotrpabich.talktactics.course.participant.word.dto.CourseParticipantWordQueryCriteria;
import com.piotrpabich.talktactics.course.participant.word.dto.CourseParticipantWordDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.COURSE_PARTICIPANT_WORDS_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + COURSE_PARTICIPANT_WORDS_PATH)
@Tag(name = "CourseParticipantWordController")
public class CourseParticipantWordController {

    private final CourseParticipantWordFacade courseParticipantWordFacade;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<Page<CourseParticipantWordDto>> queryCourseParticipantWords(
            @Valid final CourseParticipantWordQueryCriteria criteria,
            final Pageable pageable,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(courseParticipantWordFacade.queryAll(criteria, pageable, requester));
    }

    @PostMapping("/{courseParticipantWordUuid}/learn")
    public ResponseEntity<Void> learnCourseParticipantWord(
            @PathVariable final UUID courseParticipantWordUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseParticipantWordFacade.learnCourseParticipantWord(courseParticipantWordUuid, requester);
        return ResponseEntity.noContent().build();
    }
}
