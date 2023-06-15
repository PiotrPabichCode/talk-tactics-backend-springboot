package com.example.talktactics.controllers;

import com.example.talktactics.DTOs.CourseItemDTO;
import com.example.talktactics.models.CourseItem;
import com.example.talktactics.services.CourseItemService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CourseItemController {

    private final CourseItemService courseItemService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/course-items")
    List<CourseItem> getAll() {
        return courseItemService.getAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/course-items")
    List<CourseItemDTO> getAllCourseItemsDTO() {
        return courseItemService.getAllCourseItemsDTO();
    }

    @GetMapping("/course-items/{id}")
    Optional<CourseItem> getCourseItemDetailsById(@PathVariable Long id) {
        return courseItemService.findById(id);
    }


    @GetMapping("/courses/{id}/course-items")
    List<CourseItemDTO> getByCourseId(@PathVariable int id) {
        return courseItemService.getAllCourseItemsDTOByCourseId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/course-items/{id}")
    void deleteCourseItem(@PathVariable Long id) {
        courseItemService.deleteCourseItem(id);
    }
}
