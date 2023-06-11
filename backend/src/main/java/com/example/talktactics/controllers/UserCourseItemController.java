package com.example.talktactics.controllers;


import com.example.talktactics.models.UserCourseItem;
import com.example.talktactics.services.UserCourseItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserCourseItemController {

    private final UserCourseItemService userCourseItemService;

    @PostMapping("/user-course-items/{id}/isLearned")
    public void updateIsLearned(@PathVariable Long id) {
        userCourseItemService.updateIsLearned(id);
    }
}
