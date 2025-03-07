package com.piotrpabich.talktactics.user_course;

import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import com.piotrpabich.talktactics.user_course_item.entity.UserCourseItem;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class UserCourseMapper {

    List<UserCourseItem> convert(final UserCourse userCourse) {
        return userCourse.getCourse().getCourseItems().stream()
                .map(courseItem -> new UserCourseItem(userCourse, courseItem))
                .collect(toList());
    }
}
