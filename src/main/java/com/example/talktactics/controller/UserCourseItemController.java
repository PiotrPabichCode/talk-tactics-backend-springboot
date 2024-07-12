package com.example.talktactics.controller;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.user_course_item.UserCourseItemQueryCriteria;
import com.example.talktactics.dto.user_course_item.UserCourseItemDto;
import com.example.talktactics.service.user_course_item.UserCourseItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.example.talktactics.common.AppConst.API_V1;
import static com.example.talktactics.common.AppConst.USER_COURSE_ITEMS_PATH;

@RestController
@AllArgsConstructor
@RequestMapping(API_V1 + USER_COURSE_ITEMS_PATH)
@Tag(name = "User course items", description = "User course items management APIs")
public class UserCourseItemController {

    private final UserCourseItemService userCourseItemService;

    @GetMapping("/all")
    public ResponseEntity<PageResult<UserCourseItemDto>> queryUserCourseItems(@Validated UserCourseItemQueryCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(userCourseItemService.queryAll(criteria, pageable));
    }

    @PostMapping("/learn/id/{id}")
    public ResponseEntity<Object> learnUserCourseItem(@PathVariable Long id) {
        userCourseItemService.updateIsLearned(id);
        return ResponseEntity.noContent().build();
    }
}
