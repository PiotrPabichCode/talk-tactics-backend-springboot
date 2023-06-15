package com.example.talktactics.services;

import com.example.talktactics.DTOs.CourseItemDTO;
import com.example.talktactics.exceptions.CourseNotFoundException;
import com.example.talktactics.models.CourseItem;
import com.example.talktactics.repositories.CourseItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseItemService {
    private final CourseItemRepository courseItemRepository;

    public List<CourseItem> getAll() {
        return courseItemRepository.findAll();
    }

    public List<CourseItemDTO> getAllCourseItemsDTO() {
        List<CourseItem> courseItems = courseItemRepository.findAll();
        return courseItems.stream()
                .map(CourseItem::toDTO)
                .collect(Collectors.toList());
    }

    public List<CourseItemDTO> getAllCourseItemsDTOByCourseId(int id) {
        List<CourseItem> courseItems = courseItemRepository.findByCourseId(id);
        return courseItems.stream()
                .map(CourseItem::toDTO)
                .collect(Collectors.toList());
    }
    public Optional<CourseItem> findById(Long id) {
        return courseItemRepository.findById(id);
    }

    public void deleteCourseItem(Long id) {
        if (!courseItemRepository.existsById(id)) {
            throw new CourseNotFoundException(id);
        }
        courseItemRepository.deleteById(id);
    }

}
