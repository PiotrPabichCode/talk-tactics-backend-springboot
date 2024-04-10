package com.example.talktactics.controller;

import com.example.talktactics.dto.user_course.UserCoursePreviewDto;
import com.example.talktactics.dto.user_course.req.UserCourseAddReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseDeleteReqDto;
import com.example.talktactics.dto.user_course.req.UserCourseGetReqDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private List<UserCoursePreviewDto> userCoursePreviewDtoList;
    private UserCourseGetReqDto userCourseGetReqDto;
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
                .id(1)
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

        userCoursePreviewDtoList = List.of(
                UserCoursePreviewDto.builder()
                        .id(1)
                        .courseId(courseList.get(0).getId())
                        .userId(user.getId())
                        .progress(37.0)
                        .completed(false)
                        .build(),
                UserCoursePreviewDto.builder()
                        .courseId(courseList.get(1).getId())
                        .userId(user.getId()).progress(100)
                        .completed(true)
                        .build()
        );

        userCourseGetReqDto = UserCourseGetReqDto.builder()
                .userId(user.getId())
                .courseId(courseList.get(0).getId())
                .build();

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
    public void UserCourseController_GetAllUserCourses_ReturnsUserCourses() throws Exception {
        given(userCourseService.getAllUserCourses()).willReturn(userCourseList);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(userCourseList.get(0).getId()))
                .andExpect(jsonPath("$[0].course.id").value(userCourseList.get(0).getCourse().getId()))
                .andExpect(jsonPath("$[0].user.id").value(userCourseList.get(0).getUser().getId()))
                .andExpect(jsonPath("$[1].id").value(userCourseList.get(1).getId()));

        verify(userCourseService).getAllUserCourses();
    }

    @Test
    public void UserCourseController_GetUserCoursesPreviewByUserId_ReturnsUserCoursesPreview() throws Exception {
        given(userCourseService.getUserCoursesPreviewListByUserId(any(long.class))).willReturn(userCoursePreviewDtoList);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/preview/user-id/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(userCoursePreviewDtoList.get(0).getId()))
                .andExpect(jsonPath("$[0].course_id").value(userCoursePreviewDtoList.get(0).getCourseId()))
                .andExpect(jsonPath("$[0].user_id").value(userCoursePreviewDtoList.get(0).getUserId()))
                .andExpect(jsonPath("$[1].id").value(userCoursePreviewDtoList.get(1).getId()));

        verify(userCourseService).getUserCoursesPreviewListByUserId(user.getId());
    }

    @Test
    public void UserCourseController_GetById_ReturnsUserCourse() throws Exception {
        given(userCourseService.getById(any(long.class))).willReturn(userCourse);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + userCourse.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userCourse.getId()))
                .andExpect(jsonPath("$.course.id").value(userCourse.getCourse().getId()))
                .andExpect(jsonPath("$.user.id").value(userCourse.getUser().getId()));

        verify(userCourseService).getById(userCourse.getId());
    }

    @Test
    public void UserCourseController_GetAllByUserId_ReturnsUserCourses() throws Exception {
        given(userCourseService.getAllByUserId(any(long.class))).willReturn(userCourseList);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/user-id/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(userCourseList.get(0).getId()))
                .andExpect(jsonPath("$[0].course.id").value(userCourseList.get(0).getCourse().getId()))
                .andExpect(jsonPath("$[0].user.id").value(userCourseList.get(0).getUser().getId()))
                .andExpect(jsonPath("$[1].id").value(userCourseList.get(1).getId()));

        verify(userCourseService).getAllByUserId(user.getId());
    }

    @Test
    public void UserCourseController_GetByUserIdAndCourseId_ReturnsUserCourse() throws Exception {
        given(userCourseService.getByUserIdAndCourseId(any(UserCourseGetReqDto.class))).willReturn(userCourse);

        MockHttpServletRequestBuilder request = post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCourseGetReqDto));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userCourse.getId()))
                .andExpect(jsonPath("$.course.id").value(userCourse.getCourse().getId()))
                .andExpect(jsonPath("$.user.id").value(userCourse.getUser().getId()));

        verify(userCourseService).getByUserIdAndCourseId(any(UserCourseGetReqDto.class));
    }

    @Test
    public void UserCourseController_AddCourseToUser_ReturnsNoContent() throws Exception {
        doNothing().when(userCourseService).addUserCourse(any(UserCourseAddReqDto.class));

        MockHttpServletRequestBuilder request = put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCourseAddReqDto));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(userCourseService).addUserCourse(any(UserCourseAddReqDto.class));
    }

    @Test
    public void UserCourseController_Delete_ReturnsNoContent() throws Exception {
        doNothing().when(userCourseService).deleteUserCourse(any(UserCourseDeleteReqDto.class));

        MockHttpServletRequestBuilder request = delete(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCourseDeleteReqDto));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(userCourseService).deleteUserCourse(any(UserCourseDeleteReqDto.class));
    }
}
