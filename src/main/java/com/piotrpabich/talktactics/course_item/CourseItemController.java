package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.auth.AuthenticationService;
import com.piotrpabich.talktactics.course_item.dto.CourseItemDto;
import com.piotrpabich.talktactics.course_item.dto.CourseItemPreview;
import com.piotrpabich.talktactics.course_item.dto.CourseItemQueryCriteria;
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
import static com.piotrpabich.talktactics.common.AppConst.COURSE_ITEMS_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1 + COURSE_ITEMS_PATH)
@Tag(name = "CourseItemController")
public class CourseItemController {
    private final CourseItemService courseItemService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<Page<CourseItemPreview>> queryCourseItems(
            @Valid final CourseItemQueryCriteria criteria,
            final Pageable pageable
    ) {
        return ResponseEntity.ok(courseItemService.queryAll(criteria, pageable));
    }

    @GetMapping("/{courseItemUuid}")
    public ResponseEntity<CourseItemDto> getCourseItem(@PathVariable final UUID courseItemUuid) {
        return ResponseEntity.ok(courseItemService.getCourseItemByUuid(courseItemUuid));
    }

    @DeleteMapping("/{courseItemUuid}")
    public ResponseEntity<Void> deleteCourseItem(
            @PathVariable final UUID courseItemUuid,
            final HttpServletRequest request
    ) {
        final var requester = authenticationService.getUserFromRequest(request);
        courseItemService.delete(courseItemUuid, requester);
        return ResponseEntity.noContent().build();
    }
}
