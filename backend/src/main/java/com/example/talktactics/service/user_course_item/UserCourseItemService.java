package com.example.talktactics.service.user_course_item;

import com.example.talktactics.exception.UserCourseItemNotFoundException;
import com.example.talktactics.entity.UserCourse;
import com.example.talktactics.entity.UserCourseItem;
import com.example.talktactics.repository.UserCourseItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserCourseItemService {
    private final UserCourseItemRepository userCourseItemRepository;

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
}
