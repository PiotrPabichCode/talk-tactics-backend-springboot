package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.course_item.dto.CourseItemDto;
import com.piotrpabich.talktactics.course_item.dto.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.course_item.dto.CourseItemPreview;
import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;


public interface CourseItemService {
    Page<CourseItemPreview> queryAll(CourseItemQueryCriteria criteria, Pageable pageable);
    CourseItemDto getById(Long id);
    void delete(Set<Long> ids, User requester);
}
