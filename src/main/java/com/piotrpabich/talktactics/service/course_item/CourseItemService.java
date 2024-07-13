package com.piotrpabich.talktactics.service.course_item;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.course_item.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.dto.course_item.CourseItemDto;
import org.springframework.data.domain.Pageable;

import java.util.Set;


public interface CourseItemService {
    PageResult<CourseItemDto> queryAll(CourseItemQueryCriteria criteria, Pageable pageable);
    void delete(Set<Long> ids);
}
