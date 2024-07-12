package com.example.talktactics.controller;

import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.entity.*;
import com.example.talktactics.service.jwt.JwtServiceImpl;
import com.example.talktactics.service.user.UserServiceImpl;
import com.example.talktactics.service.user_course.UserCourseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserCourseController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserCourseControllerTests {

    private static final String BASE_URL = "/api/v1/user-courses";

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtServiceImpl jwtService;
    @MockBean
    private UserCourseServiceImpl userCourseService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Course> courseList;
    private User user;

    private UserCourse userCourse;
    private List<UserCourse> userCourseList;
    private UserCourseAddReqDto userCourseAddReqDto;
    private UserCourseDeleteReqDto userCourseDeleteReqDto;

    @BeforeEach
    public void init() {
        courseList = List.of(
                Course.builder()
                        .title("English for Beginners")
                        .level(CourseLevel.BEGINNER)
                        .description("A comprehensive course designed to help beginners learn English from scratch.")
                        .build(),
                Course.builder()
                        .title("Intermediate English")
                        .level(CourseLevel.INTERMEDIATE)
                        .description("A course tailored to help learners improve their English language skills.")
                        .build()
        );
        user = User.builder()
                .email("waynerooney@email.com")
                .firstName("Wayne")
                .lastName("Rooney")
                .role(Role.USER)
                .username("wayne_rooney")
                .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                .build();

        userCourse = UserCourse.builder()
                .id(1L)
                .course(courseList.get(0))
                .user(user)
                .progress(37.0)
                .completed(false)
                .build();
        userCourseList = List.of(
                UserCourse.builder()
                        .course(courseList.get(0))
                        .user(user)
                        .progress(37.0)
                        .completed(false)
                        .build(),
                UserCourse.builder()
                        .course(courseList.get(1))
                        .user(user)
                        .progress(100)
                        .completed(true)
                        .build()
        );

        userCourseAddReqDto = UserCourseAddReqDto.builder()
                .userId(user.getId())
                .courseId(courseList.get(0).getId())
                .build();

        userCourseDeleteReqDto = UserCourseDeleteReqDto.builder()
                .userId(user.getId())
                .courseId(courseList.get(0).getId())
                .build();
    }

    @Test
    public void UserCourseController_QueryUserCourses_ReturnsPageResult() throws Exception {
        // TODO: Implement this test with queryAll() functionality
//        given(userCourseService.getAllUserCourses()).willReturn(userCourseList);
//
//        MockHttpServletRequestBuilder request = get(BASE_URL + "/all")
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$[0].id").value(userCourseList.get(0).getId()))
//                .andExpect(jsonPath("$[0].course.id").value(userCourseList.get(0).getCourse().getId()))
//                .andExpect(jsonPath("$[0].user.id").value(userCourseList.get(0).getUser().getId()))
//                .andExpect(jsonPath("$[1].id").value(userCourseList.get(1).getId()));
//
//        verify(userCourseService).getAllUserCourses();
    }

    @Test
    public void UserCourseController_GetById_ReturnsUserCourse_Status200() throws Exception {
        given(userCourseService.getById(anyLong())).willReturn(userCourse);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + userCourse.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userCourse.getId()))
                .andExpect(jsonPath("$.course.id").value(userCourse.getCourse().getId()))
                .andExpect(jsonPath("$.user.id").value(userCourse.getUser().getId()));

        verify(userCourseService).getById(userCourse.getId());
    }

    @Test
    public void UserCourseController_AddCourseToUser_ReturnsNothing_Status201() throws Exception {
        doNothing().when(userCourseService).addUserCourse(any(UserCourseAddReqDto.class));

        MockHttpServletRequestBuilder request = put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCourseAddReqDto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        verify(userCourseService).addUserCourse(any(UserCourseAddReqDto.class));
    }

    @Test
    public void UserCourseController_Delete_ReturnsNothing_Status200() throws Exception {
        doNothing().when(userCourseService).deleteUserCourse(any(UserCourseDeleteReqDto.class));

        MockHttpServletRequestBuilder request = delete(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCourseDeleteReqDto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(userCourseService).deleteUserCourse(any(UserCourseDeleteReqDto.class));
    }
}
