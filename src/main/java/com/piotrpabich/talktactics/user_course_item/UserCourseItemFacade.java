package com.piotrpabich.talktactics.user_course_item;


import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemDto;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user.UserService;
import com.piotrpabich.talktactics.auth.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCourseItemFacade {

    private final UserService userService;
    private final UserCourseItemService userCourseItemService;

    public PageResult<UserCourseItemDto> queryAll(
            UserCourseItemQueryCriteria criteria,
            Pageable pageable,
            User requester
    ) {
        User user = userService.getUserById(requester.getId());
        AuthUtil.validateIfUserHimselfOrAdmin(requester, user);
        return userCourseItemService.queryAll(criteria, pageable);
    }

    @Transactional
    public void learnUserCourseItem(Long id, User requester) {
        userCourseItemService.learnUserCourseItem(id, requester);
    }

}
