package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.course_item.dto.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.course_item.dto.CourseItemDto;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.auth.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.piotrpabich.talktactics.common.QueryHelp.getPredicate;
import static com.piotrpabich.talktactics.common.util.PageUtil.toPage;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseItemServiceImpl implements CourseItemService {

    private final CourseItemRepository courseItemRepository;

    @Override
    public PageResult<CourseItemDto> queryAll(
            final CourseItemQueryCriteria criteria,
            final Pageable pageable
    ) {
        final var page = courseItemRepository.findAll(getCourseItemSpecification(criteria), pageable);
        return generatePageResult(page);
    }
    @Override
    @Transactional
    public void delete(final Set<Long> ids, final User requester) {
        AuthUtil.validateIfUserAdmin(requester);
        courseItemRepository.deleteAllById(ids);
    }

    private Specification<CourseItem> getCourseItemSpecification(
            final CourseItemQueryCriteria criteria
    ) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(
                        getPredicate(root, criteria, criteriaBuilder),
                        criteriaBuilder.equal(root.get("course").get("id"), criteria.getCourseId())
                );
    }

    private PageResult<CourseItemDto> generatePageResult(final Page<CourseItem> page) {
        Map<String, String> contentMeta = new HashMap<>();
        if(!page.getContent().isEmpty()) {
            CourseItem item = page.getContent().getFirst();
            contentMeta.put("title", item.getCourse().getTitle());
        }
        return toPage(page.map(CourseItemDto::toDto), contentMeta);
    }
}
