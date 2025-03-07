package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.course_item.dto.CourseItemDto;
import com.piotrpabich.talktactics.course_item.dto.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.course_item.dto.CourseItemPreview;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import com.piotrpabich.talktactics.exception.NotFoundException;
import com.piotrpabich.talktactics.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.piotrpabich.talktactics.auth.AuthUtil.validateIfUserAdmin;
import static com.piotrpabich.talktactics.common.QueryHelp.getPredicate;

@Service
@Log4j2
@RequiredArgsConstructor
public class CourseItemService {

    private final CourseItemRepository courseItemRepository;

    public Page<CourseItemPreview> queryAll(
            final CourseItemQueryCriteria criteria,
            final Pageable pageable
    ) {
        return courseItemRepository.findAll(getCourseItemSpecification(criteria), pageable)
                .map(CourseItemPreview::of);
    }

    public CourseItemDto getCourseItemByUuid(final UUID uuid) {
        return courseItemRepository.findByUuid(uuid)
                .map(CourseItemDto::of)
                .orElseThrow(() -> new NotFoundException(String.format("Course item with uuid: %s was not found", uuid)));
    }

    public void delete(final UUID uuid, final User requester) {
        validateIfUserAdmin(requester);
        if (!courseItemRepository.existsByUuid(uuid)) {
            throw new NotFoundException(String.format("Course item with uuid: %s was not found", uuid));
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
