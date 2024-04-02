package com.example.talktactics.service.course_item;

import com.example.talktactics.dto.course_item.CourseItemPreviewDto;
import com.example.talktactics.entity.CourseItem;

import java.util.List;

public interface CourseItemService {
    List<CourseItemPreviewDto> getAll();
    List<CourseItemPreviewDto> getAllByCourseId(int id);
    CourseItem findById(Long id);
    void deleteById(Long id);
}
