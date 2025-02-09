package com.piotrpabich.talktactics.user_course.entity;

import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user.UserRepository;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class UserCourseEntityListeners {

    @Autowired
    private ObjectFactory<UserRepository> userRepositoryProvider;

    @PostUpdate
    public void afterUpdate(final UserCourse userCourse) {
        final var user = userCourse.getUser();
        updateUserTotalPoints(user);
        this.userRepositoryProvider.getObject().save(user);
    }

    private void updateUserTotalPoints(final User user) {
        final var totalPoints = user.getUserCourses().stream()
                .mapToInt(UserCourse::getPoints)
                .sum();
        user.setTotalPoints(totalPoints);
    }
}
