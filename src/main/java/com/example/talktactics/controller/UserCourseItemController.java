package com.example.talktactics.controller;

import com.example.talktactics.dto.user_course_item.req.GetUserCourseItemsPreviewDtoRequest;
import com.example.talktactics.dto.user_course_item.res.GetUserCourseItemPreviewDtoResponse;
import com.example.talktactics.entity.UserCourseItem;
import com.example.talktactics.service.user_course_item.UserCourseItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-course-items")
@Tag(name = "User course items", description = "User course items management APIs")
public class UserCourseItemController {

    private final UserCourseItemService userCourseItemService;

    @PostMapping("/{id}/learn")
    public void updateIsLearned(@PathVariable Long id) {
        userCourseItemService.updateIsLearned(id);
    }

    @PostMapping("/preview")
    public GetUserCourseItemPreviewDtoResponse getAllByUserIdAndCourseId(@RequestBody GetUserCourseItemsPreviewDtoRequest request) {
        return userCourseItemService.getUserCourseItemPreviewDtoResponse(request);
    }

    @GetMapping("/{id}")
    public UserCourseItem getById(@PathVariable Long id) {
        return userCourseItemService.getById(id);
    }
}
