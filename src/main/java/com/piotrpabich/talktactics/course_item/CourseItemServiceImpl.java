package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.course_item.dto.CourseItemDto;
import com.piotrpabich.talktactics.course_item.dto.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.course_item.dto.CourseItemPreview;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
import com.piotrpabich.talktactics.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserAdmin;
import static com.piotrpabich.talktactics.common.QueryHelp.getPredicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseItemServiceImpl implements CourseItemService {

    private final CourseItemRepository courseItemRepository;

    @Override
    public Page<CourseItemPreview> queryAll(
            final CourseItemQueryCriteria criteria,
            final Pageable pageable
    ) {
        return courseItemRepository.findAll(getCourseItemSpecification(criteria), pageable)
                .map(CourseItemPreview::of);
    }

    @Override
    public CourseItemDto getCourseItemByUuid(final UUID uuid) {
        return courseItemRepository.findByUuid(uuid)
                .map(CourseItemDto::of)
                .orElseThrow(() -> new EntityNotFoundException(CourseItemDto.class, "uuid", uuid.toString()));
    }

    @Override
    public void delete(final UUID uuid, final User requester) {
        validateIfUserAdmin(requester);
        if (!courseItemRepository.existsByUuid(uuid)) {
            throw new EntityNotFoundException(CourseItemDto.class, "uuid", uuid.toString());
        }
        courseItemRepository.deleteByUuid(uuid);
    }

    private Specification<CourseItem> getCourseItemSpecification(
            final CourseItemQueryCriteria criteria
    ) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(
                    getPredicate(root, criteria, criteriaBuilder),
                    criteriaBuilder.equal(root.get("course").get("uuid"), criteria.getCourseUuid())
                );
    }
}
