package com.example.talktactics.controller;

import com.example.talktactics.dto.user_course_item.req.GetUserCourseItemsPreviewDtoReq;
import com.example.talktactics.dto.user_course_item.res.GetUserCourseItemPreviewDtoResponse;
import com.example.talktactics.entity.UserCourseItem;
import com.example.talktactics.exception.UserCourseItemRuntimeException;
import com.example.talktactics.service.user_course_item.UserCourseItemServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user-course-items")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"}, allowCredentials = "true")
@Tag(name = "User course items", description = "User course items management APIs")
public class UserCourseItemController {

    private final UserCourseItemServiceImpl userCourseItemServiceImpl;

    @PostMapping("/learn/id/{id}")
    public void updateIsLearned(@PathVariable Long id) {
        try {
            userCourseItemServiceImpl.updateIsLearned(id);
        } catch (UserCourseItemRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/all/preview")
    public ResponseEntity<GetUserCourseItemPreviewDtoResponse> getAllByUserIdAndCourseId(@RequestBody GetUserCourseItemsPreviewDtoReq req) {
        try {
            return ResponseEntity.ok(userCourseItemServiceImpl.getUserCourseItemPreviewDtoResponse(req));
        } catch (UserCourseItemRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserCourseItem> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userCourseItemServiceImpl.getById(id));
        } catch (UserCourseItemRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
