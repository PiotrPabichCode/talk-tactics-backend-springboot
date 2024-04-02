package com.example.talktactics.service.course_item;

import com.example.talktactics.dto.course_item.CourseItemPreviewDto;
import com.example.talktactics.exception.CourseItemRuntimeException;
import com.example.talktactics.entity.CourseItem;
import com.example.talktactics.repository.CourseItemRepository;
import com.example.talktactics.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CourseItemServiceImpl implements CourseItemService{
    private final CourseItemRepository courseItemRepository;

//  PUBLIC
    public List<CourseItemPreviewDto> getAll() throws CourseItemRuntimeException {
        List<CourseItem> courseItems = courseItemRepository.findAll();
        return courseItems.stream()
                .map(CourseItem::toDTO).toList();
    }

    public List<CourseItemPreviewDto> getAllByCourseId(int id) throws CourseItemRuntimeException {
        return courseItemRepository
                .findByCourseId(id)
                .stream()
                .map(CourseItem::toDTO).toList();
    }
    public CourseItem findById(Long id) throws CourseItemRuntimeException {
        return courseItemRepository.findById(id).orElseThrow(() -> new CourseItemRuntimeException(Constants.COURSE_ITEM_NOT_FOUND_EXCEPTION));
    }

    public void deleteById(Long id) throws CourseItemRuntimeException {
        if (!courseItemRepository.existsById(id)) {
            throw new CourseItemRuntimeException(Constants.COURSE_ITEM_NOT_FOUND_EXCEPTION);
        }
        courseItemRepository.deleteById(id);
    }

//  PRIVATE

}
