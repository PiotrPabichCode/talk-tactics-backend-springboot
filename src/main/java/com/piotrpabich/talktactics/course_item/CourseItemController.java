package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.course_item.dto.CourseItemDto;
import com.piotrpabich.talktactics.course_item.dto.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.COURSE_ITEMS_PATH;

@RestController
@AllArgsConstructor
@RequestMapping(API_V1 + COURSE_ITEMS_PATH)
@Tag(name = "Course items", description = "Course items management APIs")
public class CourseItemController {
    private final CourseItemService courseItemService;
    private final AuthenticationService authenticationService;

    @GetMapping("/all")
    public ResponseEntity<PageResult<CourseItemDto>> queryCourseItems(
            @Valid final CourseItemQueryCriteria criteria,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(courseItemService.queryAll(criteria, pageable));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCourseItems(
            @RequestBody final Set<Long> ids,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseItemService.delete(ids, requester);
        return ResponseEntity.ok().build();
    }
}
