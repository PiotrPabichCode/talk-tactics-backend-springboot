package com.example.talktactics.controller;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.course_item.CourseItemQueryCriteria;
import com.example.talktactics.dto.course_item.CourseItemDto;
import com.example.talktactics.service.course_item.CourseItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/course-items")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"}, allowCredentials = "true")
@Tag(name = "Course items", description = "Course items management APIs")
public class CourseItemController {
    private final CourseItemService courseItemService;
    @GetMapping("/all")
    public ResponseEntity<PageResult<CourseItemDto>> queryCourseItems(@Validated CourseItemQueryCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(courseItemService.queryAll(criteria, pageable));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteCourseItems(@RequestBody Set<Long> ids) {
        courseItemService.delete(ids);
        return ResponseEntity.ok().build();
    }
}
