package com.example.talktactics.controllers;

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

    @PreAuthorize("hasRole(ADMIN)")
    @GetMapping("/course-items")
    List<CourseItem> getAll() {
        return courseItemService.getAll();
    }

    @GetMapping("/course-items/{id}")
    Optional<CourseItem> getCourseItemDetailsById(@PathVariable Long id) {
        return courseItemService.findById(id);
    }


    @GetMapping("/courses/{id}/course-items")
    List<CourseItem> getByCourseId(@PathVariable int id) {
        return courseItemService.getByCourseId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/course-items/{id}")
    void deleteCourseItem(@PathVariable Long id) {
        courseItemService.deleteCourseItem(id);
    }

//    @GetMapping("/courses/{courseID}/item/{itemID}")
//    CourseItem getCourseItemByCourseIdAndId(@PathVariable Long courseID, @PathVariable int itemID) {
//        return courseItemService.getCourseItemsByCourseIdAndItemId(courseID, itemID);
//    }

//    @GetMapping("/users/{userID}/courses/{courseID}/course-items")
//    List<CourseItem> getCourseItemsByCourseId(@PathVariable Long userID, @PathVariable int courseID) {
//        return courseItemService.getUserCourseItemsByCourseId(userID, courseID);
//    }
}
