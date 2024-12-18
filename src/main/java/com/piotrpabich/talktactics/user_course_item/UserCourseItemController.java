package com.piotrpabich.talktactics.user_course_item;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemDto;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.piotrpabich.talktactics.common.AppConst.API_V1;
import static com.piotrpabich.talktactics.common.AppConst.USER_COURSE_ITEMS_PATH;

@RestController
@AllArgsConstructor
@RequestMapping(API_V1 + USER_COURSE_ITEMS_PATH)
@Tag(name = "User course items", description = "User course items management APIs")
public class UserCourseItemController {

    private final UserCourseItemFacade userCourseItemFacade;
    private final AuthenticationService authenticationService;

    @GetMapping("/all")
    public ResponseEntity<PageResult<UserCourseItemDto>> queryUserCourseItems(
            @Valid UserCourseItemQueryCriteria criteria,
            Pageable pageable,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        return ResponseEntity.ok(userCourseItemFacade.queryAll(criteria, pageable, requester));
    }

    @PostMapping("/learn/id/{id}")
    public ResponseEntity<Object> learnUserCourseItem(
            @PathVariable Long id,
            final HttpServletRequest request
    ) {
        User requester = authenticationService.getUserFromRequest(request);
        userCourseItemFacade.learnUserCourseItem(id, requester);
        return ResponseEntity.noContent().build();
    }
}
