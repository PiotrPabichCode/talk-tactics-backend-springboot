package com.example.talktactics.service.user_course_item;

import com.example.talktactics.dto.user_course_item.UserCourseItemPreviewDto;
import com.example.talktactics.dto.user_course_item.req.GetUserCourseItemsPreviewDtoRequest;
import com.example.talktactics.dto.user_course_item.res.GetUserCourseItemPreviewDtoResponse;
import com.example.talktactics.exception.UserCourseItemNotFoundException;
import com.example.talktactics.entity.UserCourse;
import com.example.talktactics.entity.UserCourseItem;
import com.example.talktactics.repository.UserCourseItemRepository;
import com.example.talktactics.service.course.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserCourseItemService {
    private final UserCourseItemRepository userCourseItemRepository;
    private final CourseService courseService;

    public UserCourseItem getById(Long id) {
        return userCourseItemRepository.findById(id).orElseThrow(() -> new UserCourseItemNotFoundException("User course item not found [id]: [%d]".formatted(id)));
    }

    public void updateIsLearned(Long id) {
        UserCourseItem userCourseItem = userCourseItemRepository.findById(id).orElseThrow(() -> new UserCourseItemNotFoundException("User course item not found"));
        UserCourse userCourse = userCourseItem.getUserCourse();
        double value = 100.0 / userCourse.getUserCourseItems().size();
        userCourse.setProgress(userCourse.getProgress() + (!userCourseItem.isLearned() ? value : -value));

        double tolerance = 0.0001;
        userCourse.setCompleted(Math.abs(userCourse.getProgress() - 100.0) < tolerance);
        userCourseItem.setLearned(!userCourseItem.isLearned());
        userCourseItemRepository.save(userCourseItem);
    }

    private List<UserCourseItemPreviewDto> getAllByUserIdAndCourseId(Long userId, Long courseId) {
        return userCourseItemRepository.findAllByUserCourseCourseIdAndUserCourseUserId(courseId, userId).stream().map(UserCourseItem::toUserCourseItemPreviewDto).collect(Collectors.toList());
    }

    public GetUserCourseItemPreviewDtoResponse getUserCourseItemPreviewDtoResponse(GetUserCourseItemsPreviewDtoRequest request) {
        List<UserCourseItemPreviewDto> items = getAllByUserIdAndCourseId(request.getUserId(), request.getCourseId());
        if(items.size() == 0) {
            throw new UserCourseItemNotFoundException("User course item does not exists [UserId, CourseId]: [%d, %d]".formatted(request.getUserId(), request.getCourseId()));
        }
        items.sort(Comparator.comparingInt(UserCourseItemPreviewDto::getId));
        String courseName = courseService.getCourseById(request.getCourseId()).getTitle();
        return new GetUserCourseItemPreviewDtoResponse(courseName, items);
    }
}
