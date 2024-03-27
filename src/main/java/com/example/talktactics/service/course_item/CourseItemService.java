package com.example.talktactics.service.course_item;

import com.example.talktactics.dto.course_item.CourseItemDto;
import com.example.talktactics.exception.CourseNotFoundException;
import com.example.talktactics.entity.CourseItem;
import com.example.talktactics.repository.CourseItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<CourseItemDto> getAllCourseItemsDTO() {
        List<CourseItem> courseItems = courseItemRepository.findAll();
        return courseItems.stream()
                .map(CourseItem::toDTO)
                .collect(Collectors.toList());
    }

    public List<CourseItemDto> getAllCourseItemsDTOByCourseId(int id) {
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
