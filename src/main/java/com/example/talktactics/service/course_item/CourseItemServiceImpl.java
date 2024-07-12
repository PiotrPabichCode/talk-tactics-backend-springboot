package com.example.talktactics.service.course_item;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.course_item.CourseItemQueryCriteria;
import com.example.talktactics.dto.course_item.CourseItemDto;
import com.example.talktactics.entity.CourseItem;
import com.example.talktactics.repository.CourseItemRepository;
import com.example.talktactics.service.user.UserService;
import com.example.talktactics.util.PageUtil;
import com.example.talktactics.util.QueryHelp;
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
public class CourseItemServiceImpl implements CourseItemService{
    private final CourseItemRepository courseItemRepository;
    private final UserService userService;

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
    public void delete(Set<Long> ids) {
        userService.validateAdmin();
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
