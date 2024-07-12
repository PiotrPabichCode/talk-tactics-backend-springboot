package com.example.talktactics.service;

import com.example.talktactics.common.PageResult;
import com.example.talktactics.dto.user_course.UserCourseDto;
import com.example.talktactics.dto.user_course.UserCourseQueryCriteria;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.entity.*;
import com.example.talktactics.exception.EntityExistsException;
import com.example.talktactics.repository.*;
import com.example.talktactics.service.course.CourseServiceImpl;
import com.example.talktactics.service.user.UserServiceImpl;
import com.example.talktactics.service.user_course.UserCourseMapper;
import com.example.talktactics.service.user_course.UserCourseServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private FriendInvitationRepository friendInvitationRepository;
    @Mock
    private UserCourseRepository userCourseRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private CourseServiceImpl courseService;
    @Mock
    private UserCourseMapper userCourseMapper;

    @InjectMocks
    private UserCourseServiceImpl userCourseService;

    private List<User> userList;
    private List<Course> courseList;
    private UserCourse userCourse;
    private List<UserCourse> userCourseList;

    @BeforeEach
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, friendInvitationRepository, passwordEncoder);
        userCourseService = new UserCourseServiceImpl(userCourseItemRepository, userCourseRepository, userService, courseService, userCourseMapper);

        userList = List.of(
                User.builder()
                        .id(1L)
                        .email("dwayne_johnson@gmail.com")
                        .firstName("Dwayne")
                        .lastName("Johnson")
                        .role(Role.USER)
                        .username("dwayne_johnson")
                        .build(),
                User.builder()
                        .id(2L)

                        .email("wayne_rooney@gmail.com")
                        .firstName("Wayne")
                        .lastName("Rooney")
                        .role(Role.ADMIN)
                        .username("wayne_rooney")
                        .build()
        );

        courseList = List.of(
                Course.builder()
                        .id(1L)
                        .title("Java")
                        .courseItems(List.of())
                        .level(CourseLevel.INTERMEDIATE)
                        .description("Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible.")
                        .build(),
                Course.builder()
                        .id(2L)
                        .title("Python")
                        .courseItems(List.of())
                        .level(CourseLevel.ADVANCED)
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
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_QueryAllBySingleUser_ReturnsPageResult() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id"));
        Page<UserCourse> page = new PageImpl<>(List.of(userCourseList.get(0)));
        UserCourseQueryCriteria criteria = UserCourseQueryCriteria.builder()
                .userIds(Set.of(1L))
                .build();
        UserCourseDto userCourseDto = UserCourseDto.from(userCourseList.get(0));

        given(userCourseRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(page);
        given(userCourseMapper.toDto(any(UserCourse.class))).willReturn(userCourseDto);

        PageResult<UserCourseDto> result = userCourseService.queryAll(criteria, pageable);

        assertThat(result).isNotNull();
        assertThat(result.content().size()).isEqualTo(1);
        assertThat(result.content().get(0)).isEqualTo(userCourseDto);
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_GetById_ReturnsUserCourse() {
        given(userCourseRepository.findById(anyLong())).willReturn(Optional.of(userCourse));

        UserCourse result = userCourseService.getById(1);

        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(userList.get(0));
        assertThat(result.getCourse()).isEqualTo(courseList.get(0));
        assertThat(result.getProgress()).isEqualTo(37.0);
        assertThat(result.isCompleted()).isFalse();
    }

    @Test
    public void UserCourseService_AddUserCourse_ThrowsEntityExistsException() {
        UserCourseAddReqDto userCourseAddReqDto = UserCourseAddReqDto
                .builder()
                .userId(userCourseList.get(0).getUser().getId())
                .courseId(userCourseList.get(0).getId())
                .build();

        given(userCourseRepository.existsByCourseIdAndUserId(anyLong(), anyLong())).willReturn(true);

        Assertions.assertThatThrownBy(() -> userCourseService.addUserCourse(userCourseAddReqDto))
                .isInstanceOf(EntityExistsException.class);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_AddUserCourse_ReturnsNothing() {
        UserCourseAddReqDto userCourseAddReqDto = UserCourseAddReqDto
                .builder()
                .userId(userCourseList.get(0).getUser().getId())
                .courseId(userCourseList.get(0).getId())
                .build();

        given(userCourseRepository.existsByCourseIdAndUserId(anyLong(), anyLong())).willReturn(false);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(userList.get(0)));
        given(courseService.getById(anyLong())).willReturn(courseList.get(0));

        userCourseService.addUserCourse(userCourseAddReqDto);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserCourseService_DeleteUserCourse_ReturnsNothing() {
        UserCourseDeleteReqDto userCourseDeleteReqDto = UserCourseDeleteReqDto
                .builder()
                .userId(userCourseList.get(0).getUser().getId())
                .courseId(userCourseList.get(0).getId())
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(userList.get(0)));
        given(userCourseRepository.findByCourseIdAndUserId(anyLong(), anyLong())).willReturn(userCourse);

        userCourseService.deleteUserCourse(userCourseDeleteReqDto);
   }

}
