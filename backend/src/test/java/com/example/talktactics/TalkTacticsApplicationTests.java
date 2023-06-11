package com.example.talktactics;

import com.example.talktactics.models.*;
import com.example.talktactics.services.UserCourseService;
import com.example.talktactics.services.UserService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AllArgsConstructor
class TalkTacticsApplicationTests {

	private final UserCourseService userCourseService;
	private final UserService userService;
	@Test
	void checkConnection() {
		User user1 = User.builder().firstName("User1").build();
		User user2 = User.builder().firstName("User2").build();
		Course course = Course.builder().name("courseName").build();
		CourseItem courseItem = CourseItem.builder().course(course).build();
		UserCourse userCourse1 = UserCourse.builder().user(user1).name("userCourse1").build();
		UserCourse userCourse2 = UserCourse.builder().user(user2).name("userCourse1").build();
		UserCourseItem userCourseItem = UserCourseItem.builder().build();


	}

}
