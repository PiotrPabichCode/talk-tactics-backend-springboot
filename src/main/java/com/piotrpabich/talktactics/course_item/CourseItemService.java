package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.course_item.dto.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.course_item.dto.CourseItemDto;
import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.Set;


public interface CourseItemService {
    PageResult<CourseItemDto> queryAll(CourseItemQueryCriteria criteria, Pageable pageable);
    void delete(Set<Long> ids, User requester);
}
