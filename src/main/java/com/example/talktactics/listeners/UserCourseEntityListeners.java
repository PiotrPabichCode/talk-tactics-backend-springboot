package com.example.talktactics.listeners;

import com.example.talktactics.entity.UserCourse;
import com.example.talktactics.entity.User;
import com.example.talktactics.entity.UserCourseItem;
import com.example.talktactics.repository.UserRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class UserCourseEntityListeners {

    @Autowired
    private ObjectFactory<UserRepository> userRepositoryProvider;


    @PrePersist
    public void beforeSave(UserCourse userCourse) {
        userCourse.setPoints(calculateTotalPoints(userCourse));
    }

    @PostPersist
    public void afterSave(UserCourse userCourse) {
        User user = userCourse.getUser();
        int totalPoints = user.getTotalPoints();
        totalPoints += userCourse.getPoints();
        user.setTotalPoints(totalPoints);
        UserRepository userRepository = this.userRepositoryProvider.getObject();
        userRepository.save(user);
    }

//    PRIVATE
    private int calculateTotalPoints(UserCourse userCourse) {
        if(userCourse.getUserCourseItems() == null) {
            return 0;
        }
        return userCourse.getUserCourseItems().stream()
                .filter(UserCourseItem::isLearned)
                .mapToInt(item -> item.getCourseItem().getPoints())
                .sum();
    }
}
