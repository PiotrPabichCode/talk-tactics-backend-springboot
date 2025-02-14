package com.piotrpabich.talktactics.user_course_item;

import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemDto;
import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCourseItemService {
    Page<UserCourseItemDto> queryAll(UserCourseItemQueryCriteria criteria, Pageable pageable);
    void learnUserCourseItem(Long id, User requester);
}
