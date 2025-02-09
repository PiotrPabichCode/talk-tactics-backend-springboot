package com.piotrpabich.talktactics.user_course_item;


import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemDto;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCourseItemFacade {

    private final UserService userService;
    private final UserCourseItemService userCourseItemService;

    public PageResult<UserCourseItemDto> queryAll(
            final UserCourseItemQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        final var user = userService.getUserById(requester.getId());
        validateIfUserHimselfOrAdmin(requester, user);
        return userCourseItemService.queryAll(criteria, pageable);
    }

    @Transactional
    public void learnUserCourseItem(final Long id, final User requester) {
        userCourseItemService.learnUserCourseItem(id, requester);
    }
}
