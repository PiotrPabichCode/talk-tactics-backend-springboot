package com.piotrpabich.talktactics.service.user_course_item;


import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.user_course_item.UserCourseItemDto;
import com.piotrpabich.talktactics.dto.user_course_item.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.entity.User;
import com.piotrpabich.talktactics.entity.UserCourse;
import com.piotrpabich.talktactics.entity.UserCourseItem;
import com.piotrpabich.talktactics.service.user.UserService;
import com.piotrpabich.talktactics.util.AuthUtil;
import com.piotrpabich.talktactics.util.QueryHelp;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
