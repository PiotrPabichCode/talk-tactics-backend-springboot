package com.piotrpabich.talktactics.listeners;

import com.piotrpabich.talktactics.entity.UserCourse;
import com.piotrpabich.talktactics.entity.UserCourseItem;
import com.piotrpabich.talktactics.repository.UserCourseRepository;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class UserCourseItemListeners {

    @Autowired
    private ObjectFactory<UserCourseRepository> userCourseRepositoryProvider;

    @PostUpdate
    public void afterUpdate(UserCourseItem userCourseItem) {
        final var userCourse = userCourseItem.getUserCourse();

        userCourse.setCompleted(isCompleted(userCourse));
        userCourse.setPoints(calculateTotalPoints(userCourse));
        userCourse.setProgress(calculateProgress(userCourse));

        UserCourseRepository userCourseRepository = this.userCourseRepositoryProvider.getObject();
        userCourseRepository.save(userCourse);
    }

    private boolean isCompleted(UserCourse userCourse) {
        return userCourse.getUserCourseItems().stream().allMatch(UserCourseItem::isLearned);
    }

    private int calculateTotalPoints(UserCourse userCourse) {
        return userCourse.getUserCourseItems().stream()
                .filter(UserCourseItem::isLearned)
                .mapToInt(item -> item.getCourseItem().getPoints())
                .sum() + (userCourse.isCompleted() ? userCourse.getPoints() : 0);
    }

    private double calculateProgress(UserCourse userCourse) {
        final var totalItems = userCourse.getUserCourseItems().size();
        final var learnedItems = (int) userCourse.getUserCourseItems().stream()
                .filter(UserCourseItem::isLearned)
                .count();
        final var progress = 100.0 * learnedItems / totalItems;
        return Math.floor(progress * 10) / 10;
    }
}
