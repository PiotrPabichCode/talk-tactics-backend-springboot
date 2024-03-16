package com.example.talktactics.controller;

import com.example.talktactics.service.user_course_item.UserCourseItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-course-items")
@Tag(name = "User course items", description = "User course items management APIs")
public class UserCourseItemController {

    private final UserCourseItemService userCourseItemService;

    @PostMapping("/{id}/isLearned")
    public void updateIsLearned(@PathVariable Long id) {
        userCourseItemService.updateIsLearned(id);
    }
}
