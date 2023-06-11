package com.example.talktactics.services;

import com.example.talktactics.exceptions.UserCourseItemNotFoundException;
import com.example.talktactics.models.UserCourse;
import com.example.talktactics.models.UserCourseItem;
import com.example.talktactics.repositories.UserCourseItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserCourseItemService {
    private final UserCourseItemRepository userCourseItemRepository;

    public UserCourseItem getUserCourseItemById(Long id) {
        return userCourseItemRepository.findById(id).orElseThrow(() -> new UserCourseItemNotFoundException("User course item not found"));
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
}
