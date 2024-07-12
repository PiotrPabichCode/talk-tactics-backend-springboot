package com.example.talktactics.service.user_course_item;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.user_course_item.UserCourseItemQueryCriteria;
import com.example.talktactics.dto.user_course_item.UserCourseItemDto;
import org.springframework.data.domain.Pageable;

public interface UserCourseItemService {
    PageResult<UserCourseItemDto> queryAll(UserCourseItemQueryCriteria criteria, Pageable pageable);
    void updateIsLearned(Long id);
}
