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
    public void afterUpdate(UserCourse userCourse) {
        User user = userCourse.getUser();
        updateUserTotalPoints(user);

        UserRepository userRepository = this.userRepositoryProvider.getObject();
        userRepository.save(user);
    }

    private void updateUserTotalPoints(User user) {
        final var totalPoints = user.getUserCourses().stream()
                .mapToInt(UserCourse::getPoints)
                .sum();
        user.setTotalPoints(totalPoints);
    }
}
