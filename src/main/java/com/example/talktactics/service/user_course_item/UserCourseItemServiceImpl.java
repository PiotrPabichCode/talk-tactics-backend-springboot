package com.example.talktactics.service.user_course_item;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.user_course_item.UserCourseItemQueryCriteria;
import com.example.talktactics.dto.user_course_item.UserCourseItemDto;
import com.example.talktactics.entity.*;
import com.example.talktactics.exception.EntityNotFoundException;
import com.example.talktactics.repository.UserCourseItemRepository;
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

@Service
@Slf4j
@AllArgsConstructor
public class UserCourseItemServiceImpl implements UserCourseItemService {
    private final UserCourseItemRepository userCourseItemRepository;
    private final UserService userService;

//  PUBLIC
    @Override
    public PageResult<UserCourseItemDto> queryAll(UserCourseItemQueryCriteria criteria,
                                                  Pageable pageable) {
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
    @Transactional(rollbackFor = Exception.class)
    public void updateIsLearned(Long id) {
        UserCourseItem userCourseItem = getById(id);
        CourseItem courseItem = userCourseItem.getCourseItem();
        UserCourse userCourse = userCourseItem.getUserCourse();

        userCourseItem.setLearned(!userCourseItem.isLearned());
        boolean allLearned = checkIfAllLearned(userCourse);
        int addedPoints = calculatePoints(userCourseItem, courseItem, allLearned, userCourse.isCompleted());

        userCourse.setPoints(userCourse.getPoints() + addedPoints);
        userCourse.setCompleted(allLearned);
        userCourse.setProgress(calculateProgress(userCourse));
        userCourse.getUser().setTotalPoints(userCourse.getUser().getTotalPoints() + addedPoints);

        userCourseItemRepository.save(userCourseItem);
    }

    //  PRIVATE
    private UserCourseItem getById(Long id) {
        UserCourseItem userCourseItem = userCourseItemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(UserCourseItem.class, "id", String.valueOf(id)));
        User user = userCourseItem.getUserCourse().getUser();
        userService.validateCredentials(user);
        return userCourseItem;
    }
    private PageResult<UserCourseItemDto> generatePageResult(Page<UserCourseItem> page) {
        Map<String, String> contentMeta = new HashMap<>();
        if(page.getContent().size() > 0) {
            UserCourseItem item = page.getContent().get(0);
            contentMeta.put("title", item.getUserCourse().getCourse().getTitle());
        }
        return PageUtil.toPage(page.map(UserCourseItemDto::toDto), contentMeta);
    }

    private boolean checkIfAllLearned(UserCourse userCourse) {
        return userCourse.getUserCourseItems().stream().allMatch(UserCourseItem::isLearned);
    }

    private int calculatePoints(UserCourseItem userCourseItem, CourseItem courseItem, boolean allLearned, boolean isCompleted) {
        int courseCompletionPoints = userCourseItem.getUserCourse().getCourse().getPoints();
        int addedPoints = userCourseItem.isLearned() ? courseItem.getPoints() : -courseItem.getPoints();
        if(!allLearned && isCompleted) {
            addedPoints -= courseCompletionPoints;
        } else if(allLearned && !isCompleted) {
            addedPoints += courseCompletionPoints;
        }
        return addedPoints;
    }

    private double calculateProgress(UserCourse userCourse) {
        if(userCourse.getUserCourseItems() == null) {
            return 0.0;
        }
        int totalItems = userCourse.getUserCourseItems().size();
        int learnedItems = (int) userCourse.getUserCourseItems().stream()
                .filter(UserCourseItem::isLearned)
                .count();
        double progress = 100.0 * learnedItems / totalItems;
        return Math.floor(progress * 10) / 10;
    }
}
