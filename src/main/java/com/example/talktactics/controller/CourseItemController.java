package com.example.talktactics.controller;

import com.example.talktactics.dto.course_item.CourseItemDto;
import com.example.talktactics.entity.CourseItem;
import com.example.talktactics.service.course_item.CourseItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/course-items")
@CrossOrigin(origins = {"http://localhost:3000", "https://talk-tactics-frontend.vercel.app/"})
@Tag(name = "Course items", description = "Course items management APIs")
public class CourseItemController {

    private final CourseItemService courseItemService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    List<CourseItem> getAll() {
        return courseItemService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/course-items")
    List<CourseItemDto> getAllCourseItemsDTO() {
        return courseItemService.getAllCourseItemsDTO();
    }

    @GetMapping("/{id}")
    Optional<CourseItem> getCourseItemDetailsById(@PathVariable Long id) {
        return courseItemService.findById(id);
    }


    @GetMapping("/courses/{id}/course-items")
    List<CourseItemDto> getByCourseId(@PathVariable int id) {
        return courseItemService.getAllCourseItemsDTOByCourseId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    void deleteCourseItem(@PathVariable Long id) {
        courseItemService.deleteCourseItem(id);
    }
}
