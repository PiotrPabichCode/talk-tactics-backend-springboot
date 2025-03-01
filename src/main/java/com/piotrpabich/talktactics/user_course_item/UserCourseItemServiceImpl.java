package com.piotrpabich.talktactics.user_course_item;

import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemQueryCriteria;
import com.piotrpabich.talktactics.user_course_item.dto.UserCourseItemDto;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserHimselfOrAdmin;
import static com.piotrpabich.talktactics.common.QueryHelp.getPredicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCourseItemServiceImpl implements UserCourseItemService {

    private final UserCourseItemRepository userCourseItemRepository;

    @Override
    public Page<UserCourseItemDto> queryAll(
            final UserCourseItemQueryCriteria criteria,
            final Pageable pageable
    ) {
        return userCourseItemRepository.findAll(getUserCourseItemSpecification(criteria), pageable)
                .map(UserCourseItemDto::of);
    }

    @Override
    public void learnUserCourseItem(final UUID userCourseItemUuid, final User requester) {
        final var userCourseItem = getUserCourseItemByUuid(userCourseItemUuid);
        final var user = userCourseItem.getUserCourse().getUser();
        validateIfUserHimselfOrAdmin(requester, user);

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
                    criteriaBuilder.equal(userCourseRoot.get("course").get("uuid"), criteria.getCourseUuid()),
                    criteriaBuilder.equal(userCourseRoot.get("user").get("uuid"), criteria.getUserUuid())
            );
        };
    }

    private UserCourseItem getUserCourseItemByUuid(final UUID userCourseItemUuid) {
        return userCourseItemRepository.findByUuid(userCourseItemUuid)
                .orElseThrow(() -> new EntityNotFoundException(
                        UserCourseItem.class,
                        "uuid",
                        String.valueOf(userCourseItemUuid)));
    }
}
