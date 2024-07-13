package com.piotrpabich.talktactics.controller;

import com.piotrpabich.talktactics.config.CorsConfig;
import com.piotrpabich.talktactics.entity.Course;
import com.piotrpabich.talktactics.entity.CourseItem;
import com.piotrpabich.talktactics.entity.CourseLevel;
import com.piotrpabich.talktactics.service.course.CourseServiceImpl;
import com.piotrpabich.talktactics.service.course_item.CourseItemServiceImpl;
import com.piotrpabich.talktactics.service.jwt.JwtServiceImpl;
import com.piotrpabich.talktactics.service.user.UserServiceImpl;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseItemController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CourseItemControllerTests {

    private static final String BASE_URL = "/api/v1/course-items";

    @MockBean
    private CorsConfig corsConfig;
    @MockBean
    private CourseItemServiceImpl courseItemService;
    @MockBean
    private CourseServiceImpl courseService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private JwtServiceImpl jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CourseItem courseItem;
    private List<CourseItem> courseItemList;

    @BeforeEach
    public void init() {
        courseItem = CourseItem.builder()
                .id(1L)
                .word("word")
                .phonetic("phonetic")
                .level(CourseLevel.BEGINNER)
                .partOfSpeech("partOfSpeech")
                .build();

        courseItemList = List.of(
                CourseItem.builder()
                        .id(1L)
                        .course(Course.builder().id(1L).build())
                        .word("word")
                        .phonetic("phonetic")
                        .level(CourseLevel.INTERMEDIATE)
                        .partOfSpeech("partOfSpeech")
                        .build(),
                CourseItem.builder()
                        .id(2L)
                        .course(Course.builder().id(2L).build())
                        .word("word2")
                        .phonetic("phonetic2")
                        .level(CourseLevel.ADVANCED)
                        .partOfSpeech("partOfSpeech2")
                        .build()
        );

    }

    @Test
    public void CourseItemController_DeleteCourseItems_Status200() throws Exception {
        Set<Long> ids = Set.of(3L);
        doNothing().when(courseItemService).delete(anySet());

        MockHttpServletRequestBuilder request = delete(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ids));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        verify(courseItemService).delete(anySet());
    }
}
