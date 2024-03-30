package com.example.talktactics.controller;

import com.example.talktactics.dto.course_item.CourseItemPreviewDto;
import com.example.talktactics.entity.CourseItem;
import com.example.talktactics.exception.CourseItemRuntimeException;
import com.example.talktactics.service.course_item.CourseItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/course-items")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"}, allowCredentials = "true")
@Tag(name = "Course items", description = "Course items management APIs")
public class CourseItemController {

    private final CourseItemService courseItemService;

    @GetMapping("/all")
    public ResponseEntity<List<CourseItemPreviewDto>> getAll() {

        try {
            return ResponseEntity.ok(courseItemService.getAll());
        } catch(CourseItemRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CourseItem> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(courseItemService.findById(id));
        } catch(CourseItemRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/preview/courses/id/{id}")
    public ResponseEntity<List<CourseItemPreviewDto>> getPreviewListByCourseId(@PathVariable int id) {
        try {
            return ResponseEntity.ok(courseItemService.getAllByCourseId(id));
        } catch(CourseItemRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public void deleteById(@PathVariable Long id) {
        try {
            courseItemService.deleteById(id);
        } catch(CourseItemRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
