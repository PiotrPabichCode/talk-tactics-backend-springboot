package com.piotrpabich.talktactics.user_course_item;

import com.piotrpabich.talktactics.user.UserService;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemDto;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserCourseItemFacade {

    private final UserService userService;
    private final UserCourseItemService userCourseItemService;

    public Page<UserCourseItemDto> queryAll(
            final UserCourseItemQueryCriteria criteria,
            final Pageable pageable,
            final User requester
    ) {
        final var user = userService.getUserByUuid(requester.getUuid());
        validateIfUserHimselfOrAdmin(requester, user);
        return userCourseItemService.queryAll(criteria, pageable);
    }

    @Transactional
    public void learnUserCourseItem(final UUID userCourseItemUuid, final User requester) {
        userCourseItemService.learnUserCourseItem(userCourseItemUuid, requester);
    }
}
