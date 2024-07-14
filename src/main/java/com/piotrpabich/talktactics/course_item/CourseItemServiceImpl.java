package com.piotrpabich.talktactics.course_item;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.course_item.dto.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.course_item.dto.CourseItemDto;
import com.piotrpabich.talktactics.course_item.entity.CourseItem;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.auth.AuthUtil;
import com.piotrpabich.talktactics.common.util.PageUtil;
import com.piotrpabich.talktactics.common.QueryHelp;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class CourseItemServiceImpl implements CourseItemService {
    private final CourseItemRepository courseItemRepository;

//  PUBLIC
    @Override
    public PageResult<CourseItemDto> queryAll(CourseItemQueryCriteria criteria, Pageable pageable) {
        Page<CourseItem> page = courseItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            Predicate filters = QueryHelp.getPredicate(root, criteria, criteriaBuilder);
            return criteriaBuilder.and(
                    filters,
                    criteriaBuilder.equal(root.get("course").get("id"), criteria.getCourseId()));
        }, pageable);
        return generatePageResult(page);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids, User requester) {
        AuthUtil.validateIfUserAdmin(requester);
        for(Long id: ids) {
            courseItemRepository.deleteById(id);
        }
    }

//  PRIVATE
    private PageResult<CourseItemDto> generatePageResult(Page<CourseItem> page) {
        Map<String, String> contentMeta = new HashMap<>();
        if(page.getContent().size() > 0) {
            CourseItem item = page.getContent().get(0);
            contentMeta.put("title", item.getCourse().getTitle());
        }
        return PageUtil.toPage(page.map(CourseItemDto::toDto), contentMeta);
    }
}
