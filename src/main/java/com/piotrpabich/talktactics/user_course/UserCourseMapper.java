package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.course.entity.Course;
import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class UserCourseMapper {

    UserCourse convert(final User user, final Course course) {
        final var userCourse = new UserCourse();
        userCourse.setUser(user);
        userCourse.setCourse(course);
        return userCourse;
    }

    List<UserCourseItem> convert(final UserCourse userCourse) {
        return userCourse.getCourse().getCourseItems().stream()
                .map(courseItem -> UserCourseItem.builder()
                        .courseItem(courseItem)
                        .userCourse(userCourse)
                        .build()
                )
                .collect(toList());
    }
}
