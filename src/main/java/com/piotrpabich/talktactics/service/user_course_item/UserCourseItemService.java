package com.piotrpabich.talktactics.service.user_course_item;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.user_course_item.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.dto.user_course_item.UserCourseItemDto;
import com.piotrpabich.talktactics.entity.User;
import org.springframework.data.domain.Pageable;

public interface UserCourseItemService {
    PageResult<UserCourseItemDto> queryAll(UserCourseItemQueryCriteria criteria, Pageable pageable);
    void learnUserCourseItem(Long id, User requester);
}
