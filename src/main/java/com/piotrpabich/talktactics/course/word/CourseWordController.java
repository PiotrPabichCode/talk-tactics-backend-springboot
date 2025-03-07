package com.piotrpabich.talktactics.course.word;

import com.piotrpabich.talktactics.auth.AuthenticationService;
import com.piotrpabich.talktactics.course.word.dto.CourseWordDto;
import com.piotrpabich.talktactics.course.word.dto.CourseWordPreview;
import com.piotrpabich.talktactics.course.word.dto.CourseWordQueryCriteria;
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
import static com.piotrpabich.talktactics.common.AppConst.COURSE_WORDS_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + COURSE_WORDS_PATH)
@Tag(name = "CourseItemController")
public class CourseWordController {
    private final CourseWordService courseWordService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<Page<CourseWordPreview>> queryCourseWords(
            @Valid final CourseWordQueryCriteria criteria,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(courseWordService.queryAll(criteria, pageable));
    }

    @GetMapping("/{courseWordUuid}")
    public ResponseEntity<CourseWordDto> getCourseWord(@PathVariable final UUID courseWordUuid) {
        return ResponseEntity.ok(courseWordService.getCourseWordByUuid(courseWordUuid));
    }

    @DeleteMapping("/{courseWordUuid}")
    public ResponseEntity<Void> deleteCourseWord(
            @PathVariable final UUID courseWordUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseWordService.delete(courseWordUuid, requester);
        return ResponseEntity.noContent().build();
    }
}
