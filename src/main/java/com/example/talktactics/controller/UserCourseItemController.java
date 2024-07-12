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

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-course-items")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"}, allowCredentials = "true")
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
