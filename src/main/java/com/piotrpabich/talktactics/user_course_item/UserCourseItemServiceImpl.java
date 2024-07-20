package com.piotrpabich.talktactics.user_course_item;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemDto;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import com.piotrpabich.talktactics.auth.AuthUtil;
import com.piotrpabich.talktactics.common.util.PageUtil;
import com.piotrpabich.talktactics.common.QueryHelp;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class UserCourseItemServiceImpl implements UserCourseItemService {
    private final UserCourseItemRepository userCourseItemRepository;

//  PUBLIC
    @Override
    public PageResult<UserCourseItemDto> queryAll(
            UserCourseItemQueryCriteria criteria,
            Pageable pageable
    ) {
        Page<UserCourseItem> page = userCourseItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            Predicate filters = QueryHelp.getPredicate(root, criteria, criteriaBuilder);
            return criteriaBuilder.and(
                    filters,
                    criteriaBuilder.equal(root.get("userCourse").get("course").get("id"), criteria.getCourseId()),
                    criteriaBuilder.equal(root.get("userCourse").get("user").get("id"), criteria.getUserId()));
        }, pageable);
        return generatePageResult(page);
    }

    @Override
    public void learnUserCourseItem(Long id, User requester) {
        UserCourseItem userCourseItem = getById(id);
        UserCourse userCourse = userCourseItem.getUserCourse();
        AuthUtil.validateIfUserHimselfOrAdmin(requester, userCourse.getUser());

        if(userCourseItem.isLearned()) {
            throw new IllegalArgumentException("UserCourseItem already learned");
        }

        userCourseItem.setLearned(true);
        userCourseItemRepository.save(userCourseItem);
    }

    //  PRIVATE

    private UserCourseItem getById(Long id) {
        return userCourseItemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(UserCourseItem.class, "id", String.valueOf(id)));
    }
    private PageResult<UserCourseItemDto> generatePageResult(Page<UserCourseItem> page) {
        Map<String, String> contentMeta = new HashMap<>();
        if(page.getContent().size() > 0) {
            UserCourseItem item = page.getContent().get(0);
            contentMeta.put("title", item.getUserCourse().getCourse().getTitle());
        }
        return PageUtil.toPage(page.map(UserCourseItemDto::toDto), contentMeta);
    }
}
