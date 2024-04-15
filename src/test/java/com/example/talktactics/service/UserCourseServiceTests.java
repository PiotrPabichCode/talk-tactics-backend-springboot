package com.example.talktactics.service;

import com.example.talktactics.dto.user_course.UserCoursePreviewDto;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseGetReqDto;
import com.example.talktactics.dto.user_course.res.UserCourseResponseDto;
import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.Role;
import com.example.talktactics.entity.User;
import com.example.talktactics.entity.UserCourse;
import com.example.talktactics.exception.UserCourseRuntimeException;
import com.example.talktactics.repository.CourseRepository;
import com.example.talktactics.repository.UserCourseItemRepository;
import com.example.talktactics.repository.UserCourseRepository;
import com.example.talktactics.repository.UserRepository;
import com.example.talktactics.service.course.CourseServiceImpl;
import com.example.talktactics.service.user.UserServiceImpl;
import com.example.talktactics.service.user_course.UserCourseServiceImpl;
import com.example.talktactics.util.Constants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringJUnitConfig
public class UserCourseServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserCourseItemRepository userCourseItemRepository;
    @Mock
    private UserCourseRepository userCourseRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private CourseServiceImpl courseService;

    @InjectMocks
    private UserCourseServiceImpl userCourseService;

    private List<User> userList;
    private List<Course> courseList;
    private UserCourse userCourse;
    private List<UserCourse> userCourseList;
    private UserCourseAddReqDto userCourseAddReqDto;
    private UserCourseDeleteReqDto userCourseDeleteReqDto;
    private UserCourseGetReqDto userCourseGetReqDto;
    private UserCourseResponseDto userCourseResponseDto;

    @BeforeEach
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, passwordEncoder);
        userCourseService = new UserCourseServiceImpl(userCourseItemRepository, userCourseRepository, userService, courseService);

        userList = List.of(
                User.builder()
                        .email("dwayne_johnson@gmail.com")
                        .firstName("Dwayne")
                        .lastName("Johnson")
                        .role(Role.USER)
                        .username("dwayne_johnson")
                        .bio("Devoted language aficionado committed to unraveling the intricacies of English fluency, one word at a time. Embark on a voyage of linguistic discovery and expertise with me!")
                        .build(),
                User.builder()
                        .email("wayne_rooney@gmail.com")
                        .firstName("Wayne")
                        .lastName("Rooney")
                        .role(Role.ADMIN)
                        .username("wayne_rooney")
                        .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                        .build()
        );

        courseList = List.of(
                Course.builder()
                        .id(1L)
                        .title("Java")
                        .courseItems(List.of())
                        .description("Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible.")
                        .build(),
                Course.builder()
                        .id(2L)
                        .title("Python")
                        .courseItems(List.of())
                        .description("Python is an interpreted, high-level and general-purpose programming language.")
                        .build()
        );

        userCourse = UserCourse.builder()
                .progress(37.0)
                .user(userList.get(0))
                .course(courseList.get(0))
                .completed(false)
                .build();

        userCourseList = List.of(
                UserCourse.builder()
                        .id(1L)
                        .user(userList.get(0))
                        .course(courseList.get(0))
                        .progress(37.0)
                        .completed(false)
                        .build(),
                UserCourse.builder()
                        .id(2L)
                        .user(userList.get(1))
                        .course(courseList.get(1))
                        .progress(100)
                        .completed(true)
                        .build()
        );

        userList.get(0).setUserCourses(List.of(userCourseList.get(0)));

        userCourseAddReqDto = UserCourseAddReqDto
                .builder()
                .userId(userCourseList.get(0).getUser().getId())
                .courseId(userCourseList.get(0).getId())
                .build();
        userCourseDeleteReqDto = UserCourseDeleteReqDto
                .builder()
                .userId(userCourseList.get(0).getUser().getId())
                .courseId(userCourseList.get(0).getId())
                .build();
        userCourseGetReqDto = UserCourseGetReqDto
                .builder()
                .userId(userCourseList.get(0).getUser().getId())
                .courseId(userCourseList.get(0).getId())
                .build();

        userCourseResponseDto = userCourseList.get(0).toUserCourseResponseDto();
    }

//    @Test
//    public void UserCourseService_CreateUserCourse_ReturnsUserCourse() {
//        given(userService.getUserById(1L)).willReturn(userList.get(0));
//        given(courseService.getById(1L)).willReturn(courseList.get(0));
//        given(userCourseRepository.existsByCourseIdAndUserId(1L, 1L)).willReturn(false);
//        given(userCourseRepository.save(userCourse)).willReturn(userCourse);
//
//        UserCourse newUserCourse = userCourseService.addUserCourse(UserCourseAddReqDto.builder().userId(1L).courseId(1L).build());
//
//        Assertions.assertThat(newUserCourse).isNotNull();
//        Assertions.assertThat(newUserCourse.getId()).isEqualTo(1L);
//        Assertions.assertThat(newUserCourse.getUser()).isEqualTo(userList.get(0));
//        Assertions.assertThat(newUserCourse.getCourse()).isEqualTo(courseList.get(0));
//        Assertions.assertThat(newUserCourse.getProgress()).isEqualTo(37.0);
//        Assertions.assertThat(newUserCourse.isCompleted()).isFalse();
//    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void UserCourseService_GetUserCourses_ReturnsListOfUserCourses() {
        given(userCourseRepository.findAll(Sort.by("id"))).willReturn(userCourseList);

        List<UserCourse> result = userCourseService.getAllUserCourses();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_GetAllByUserId_ReturnsListOfUserCourses() {
        UserCourse testUserCourse = userCourseList.get(0);
        given(userRepository.findById(any(long.class))).willReturn(Optional.of(userList.get(0)));
        given(userCourseRepository.findAllByUserId(any(long.class))).willReturn(List.of(testUserCourse));

        List<UserCourseResponseDto> result = userCourseService.getAllByUserId(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0)).isEqualTo(userCourseResponseDto);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_GetUserCoursesPreviewListByUserId_ReturnsListOfUserCoursePreviewDtos() {
        given(userRepository.findById(any(long.class))).willReturn(Optional.of(userList.get(0)));

        List<UserCoursePreviewDto> userCoursePreviewDtoList = userCourseService.getUserCoursesPreviewListByUserId(1);

        Assertions.assertThat(userCoursePreviewDtoList).isNotNull();
        Assertions.assertThat(userCoursePreviewDtoList.size()).isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_GetById_ReturnsUserCourse() {
        given(userCourseRepository.findById(any(long.class))).willReturn(Optional.of(userCourse));

        UserCourse result = userCourseService.getById(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUser()).isEqualTo(userList.get(0));
        Assertions.assertThat(result.getCourse()).isEqualTo(courseList.get(0));
        Assertions.assertThat(result.getProgress()).isEqualTo(37.0);
        Assertions.assertThat(result.isCompleted()).isFalse();
    }

    @Test
    public void UserCourseService_AddUserCourse_ThrowsUserCourseRuntimeException() {
        given(userCourseRepository.existsByCourseIdAndUserId(any(long.class), any(long.class))).willReturn(true);

        Assertions.assertThatThrownBy(() -> userCourseService.addUserCourse(UserCourseAddReqDto.builder().userId(1L).courseId(1L).build()))
                .isInstanceOf(UserCourseRuntimeException.class)
                .hasMessage(Constants.USER_COURSE_EXISTS_EXCEPTION);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_AddUserCourse_ReturnsNothing() {
        given(userCourseRepository.existsByCourseIdAndUserId(any(long.class), any(long.class))).willReturn(false);
        given(userRepository.findById(any(long.class))).willReturn(Optional.of(userList.get(0)));
        given(courseService.getById(any(long.class))).willReturn(courseList.get(0));

        userCourseService.addUserCourse(userCourseAddReqDto);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_DeleteUserCourse_ReturnsNothing() {
       given(userRepository.findById(any(long.class))).willReturn(Optional.of(userList.get(0)));
       given(userCourseRepository.findByCourseIdAndUserId(any(long.class), any(long.class))).willReturn(userCourse);

       userCourseService.deleteUserCourse(userCourseDeleteReqDto);
   }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_GetByUserIdAndCourseId_ReturnsUserCourse() {
        given(userRepository.findById(any(long.class))).willReturn(Optional.of(userList.get(0)));
        given(userCourseRepository.findByCourseIdAndUserId(any(long.class), any(long.class))).willReturn(userCourse);
        UserCourse result = userCourseService.getByUserIdAndCourseId(UserCourseGetReqDto.builder().userId(1L).courseId(1L).build());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUser()).isEqualTo(userList.get(0));
        Assertions.assertThat(result.getCourse()).isEqualTo(courseList.get(0));
        Assertions.assertThat(result.getProgress()).isEqualTo(37.0);
        Assertions.assertThat(result.isCompleted()).isFalse();
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_GetByUserIdAndCourseId_ThrowsUserCourseRuntimeException() {
        given(userRepository.findById(any(long.class))).willReturn(Optional.of(userList.get(0)));
        given(userCourseRepository.findByCourseIdAndUserId(any(long.class), any(long.class))).willReturn(null);
        Assertions.assertThatThrownBy(() -> userCourseService.getByUserIdAndCourseId(UserCourseGetReqDto.builder().userId(1L).courseId(1L).build()))
                .isInstanceOf(UserCourseRuntimeException.class)
                .hasMessage(Constants.USER_COURSE_NOT_FOUND_EXCEPTION);
    }

}
