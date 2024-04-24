package com.example.talktactics.service.user_course_item;

import com.example.talktactics.dto.user_course_item.UserCourseItemPreviewDto;
import com.example.talktactics.dto.user_course_item.req.GetUserCourseItemsPreviewDtoReq;
import com.example.talktactics.dto.user_course_item.res.GetUserCourseItemPreviewDtoResponse;
import com.example.talktactics.dto.user_course_item.res.LearnUserCourseItemDtoResponse;
import com.example.talktactics.entity.*;
import com.example.talktactics.exception.UserCourseItemRuntimeException;
import com.example.talktactics.exception.UserCourseRuntimeException;
import com.example.talktactics.repository.UserCourseItemRepository;
import com.example.talktactics.service.course.CourseService;
import com.example.talktactics.service.user.UserService;
import com.example.talktactics.util.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserCourseItemServiceImpl implements UserCourseItemService {
    private final UserCourseItemRepository userCourseItemRepository;
    private final CourseService courseService;
    private final UserService userService;

//  PUBLIC
    @Override
    public UserCourseItem getById(Long id) {
        UserCourseItem userCourseItem = userCourseItemRepository.findById(id).orElseThrow(() -> new UserCourseItemRuntimeException(Constants.USER_COURSE_ITEM_NOT_FOUND_EXCEPTION));
        User user = userCourseItem.getUserCourse().getUser();
        userService.validateCredentials(user);
        return userCourseItem;
    }
    @Override
    public LearnUserCourseItemDtoResponse updateIsLearned(Long id) {
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

        return new LearnUserCourseItemDtoResponse(userCourse.getCourse().getId());
    }
    @Override
    public GetUserCourseItemPreviewDtoResponse getUserCourseItemPreviewDtoResponse(GetUserCourseItemsPreviewDtoReq req) {
        List<UserCourseItemPreviewDto> items = getAllByUserIdAndCourseId(req.getUserId(), req.getCourseId());
        String courseName = courseService.getById(req.getCourseId()).getTitle();
        return new GetUserCourseItemPreviewDtoResponse(courseName, items);
    }

//  PRIVATE
    private List<UserCourseItemPreviewDto> getAllByUserIdAndCourseId(Long userId, Long courseId) {
        User user = userService.getUserById(userId);
        userService.validateCredentials(user);
        List<UserCourseItemPreviewDto> items = userCourseItemRepository.findAllByUserCourseCourseIdAndUserCourseUserId(courseId, userId).stream().map(UserCourseItem::toUserCourseItemPreviewDto).collect(Collectors.toList());
        if(items.isEmpty()) {
            throw new UserCourseRuntimeException(Constants.USER_COURSE_ITEM_NOT_FOUND_EXCEPTION);
        }
        items.sort(Comparator.comparingLong(UserCourseItemPreviewDto::getId));
        return items;
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
