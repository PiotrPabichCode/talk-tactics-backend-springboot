package com.piotrpabich.talktactics.user_course_item;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemDto;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import com.piotrpabich.talktactics.common.util.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;
import static com.piotrpabich.talktactics.common.QueryHelp.getPredicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCourseItemServiceImpl implements UserCourseItemService {

    private final UserCourseItemRepository userCourseItemRepository;

    @Override
    public PageResult<UserCourseItemDto> queryAll(
            final UserCourseItemQueryCriteria criteria,
            final Pageable pageable
    ) {
        final var page = userCourseItemRepository.findAll(getUserCourseItemSpecification(criteria), pageable);
        return generatePageResult(page);
    }

    @Override
    public void learnUserCourseItem(final Long id, final User requester) {
        final var userCourseItem = getById(id);
        final var userCourse = userCourseItem.getUserCourse();
        validateIfUserHimselfOrAdmin(requester, userCourse.getUser());

        if (userCourseItem.isLearned()) {
            throw new IllegalArgumentException("UserCourseItem already learned");
        }

        userCourseItem.setLearned(true);
        userCourseItemRepository.save(userCourseItem);
    }

    private Specification<UserCourseItem> getUserCourseItemSpecification(
            final UserCourseItemQueryCriteria criteria
    ) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final var userCourseRoot = root.get("userCourse");
            return criteriaBuilder.and(
                    getPredicate(root, criteria, criteriaBuilder),
                    criteriaBuilder.equal(userCourseRoot.get("course").get("id"), criteria.getCourseId()),
                    criteriaBuilder.equal(userCourseRoot.get("user").get("id"), criteria.getUserId())
            );
        };
    }

    private UserCourseItem getById(final Long id) {
        return userCourseItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UserCourseItem.class, "id", String.valueOf(id)));
    }

    private PageResult<UserCourseItemDto> generatePageResult(final Page<UserCourseItem> page) {
        Map<String, String> contentMeta = new HashMap<>();
        if (!page.getContent().isEmpty()) {
            final var title = page.getContent().getFirst()
                    .getUserCourse()
                    .getCourse()
                    .getTitle();
            contentMeta.put("title", title);
        }
        return PageUtil.toPage(page.map(UserCourseItemDto::toDto), contentMeta);
    }
}
