package com.example.talktactics.listeners;

import com.example.talktactics.entity.UserCourse;
import com.example.talktactics.entity.User;
import com.example.talktactics.entity.UserCourseItem;
import com.example.talktactics.repository.UserRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import lombok.NonNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class UserCourseEntityListeners {

    @Autowired
    private ObjectFactory<UserRepository> userRepositoryProvider;


    @PrePersist
    public void beforeSave(@NonNull UserCourse userCourse) {
        userCourse.setPoints(calculateTotalPoints(userCourse));
        userCourse.setProgress(calculateProgress(userCourse));
    }

    @PostPersist
    public void afterSave(@NonNull UserCourse userCourse) {
        User user = userCourse.getUser();
        int totalPoints = user.getTotalPoints();
        totalPoints += userCourse.getPoints();
        user.setTotalPoints(totalPoints);
        UserRepository userRepository = this.userRepositoryProvider.getObject();
        userRepository.save(user);
    }

//    PRIVATE
    private int calculateTotalPoints(@NonNull UserCourse userCourse) {
        if(userCourse.getUserCourseItems() == null) {
            return 0;
        }
        return userCourse.getUserCourseItems().stream()
                .filter(UserCourseItem::isLearned)
                .mapToInt(item -> item.getCourseItem().getPoints())
                .sum() + (userCourse.isCompleted() ? userCourse.getPoints() : 0);
    }

    private double calculateProgress(@NonNull UserCourse userCourse) {
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
