package com.example.talktactics.controller;

import com.example.talktactics.dto.course_item.CourseItemPreviewDto;
import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseItem;
import com.example.talktactics.entity.CourseLevel;
import com.example.talktactics.exception.CourseItemRuntimeException;
import com.example.talktactics.service.course.CourseServiceImpl;
import com.example.talktactics.service.course_item.CourseItemServiceImpl;
import com.example.talktactics.service.jwt.JwtServiceImpl;
import com.example.talktactics.service.user.UserServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseItemController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CourseItemControllerTests {

    private static final String BASE_URL = "/api/v1/course-items";

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
    private List<CourseItemPreviewDto> courseItemPreviewDtoList;

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
                        .id(1)
                        .course(Course.builder().id(1).build())
                        .word("word")
                        .phonetic("phonetic")
                        .level(CourseLevel.INTERMEDIATE)
                        .partOfSpeech("partOfSpeech")
                        .build(),
                CourseItem.builder()
                        .id(2)
                        .course(Course.builder().id(2).build())
                        .word("word2")
                        .phonetic("phonetic2")
                        .level(CourseLevel.ADVANCED)
                        .partOfSpeech("partOfSpeech2")
                        .build()
        );

        courseItemPreviewDtoList = List.of(
                CourseItemPreviewDto.builder()
                        .id(1)
                        .word("word")
                        .phonetic("phonetic")
                        .partOfSpeech("partOfSpeech")
                        .courseName("courseTitle")
                        .build(),
                CourseItemPreviewDto.builder()
                        .id(2)
                        .word("word2")
                        .phonetic("phonetic2")
                        .partOfSpeech("partOfSpeech2")
                        .courseName("courseTitle2")
                        .build()
        );

    }

    @Test
    public void CourseItemController_GetAll_ReturnsCourseItems_Status200() throws Exception {
        given(courseItemService.getAll()).willReturn(courseItemPreviewDtoList);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].course_name").value(courseItemPreviewDtoList.get(0).getCourseName()))
                .andExpect(jsonPath("$[1].course_name").value(courseItemPreviewDtoList.get(1).getCourseName()));

        verify(courseItemService).getAll();
    }

    @Test
    public void CourseItemController_GetAll_Status404() throws Exception {
        given(courseItemService.getAll()).willThrow(new CourseItemRuntimeException("Course items not found."));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Course items not found.\"", result.getResolvedException().getMessage()));

        verify(courseItemService).getAll();
    }

    @Test
    public void CourseItemController_GetById_ReturnsCourseItem_Status200() throws Exception {
        given(courseItemService.findById(any(long.class))).willReturn(courseItem);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + courseItem.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(courseItem.getId()))
                .andExpect(jsonPath("$.word").value(courseItem.getWord()))
                .andExpect(jsonPath("$.phonetic").value(courseItem.getPhonetic()))
                .andExpect(jsonPath("$.part_of_speech").value(courseItem.getPartOfSpeech()));

        verify(courseItemService).findById(1L);
    }

    @Test
    public void CourseItemController_GetById_Status404() throws Exception {
        given(courseItemService.findById(any(long.class))).willThrow(new CourseItemRuntimeException("Course item not found."));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + courseItem.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Course item not found.\"", result.getResolvedException().getMessage()));

        verify(courseItemService).findById(any(long.class));
    }

    @Test
    public void CourseItemController_GetPreviewListByCourseId_ReturnsCourseItems_Status200() throws Exception {
        given(courseItemService.getAllByCourseId(any(long.class))).willReturn(courseItemPreviewDtoList);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/preview/courses/id/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].course_name").value(courseItemPreviewDtoList.get(0).getCourseName()))
                .andExpect(jsonPath("$[1].course_name").value(courseItemPreviewDtoList.get(1).getCourseName()));

        verify(courseItemService).getAllByCourseId(any(long.class));
    }

    @Test
    public void CourseItemController_GetPreviewListByCourseId_Status404() throws Exception {
        given(courseItemService.getAllByCourseId(any(long.class))).willThrow(new CourseItemRuntimeException("Course items not found."));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/preview/courses/id/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Course items not found.\"", result.getResolvedException().getMessage()));

        verify(courseItemService).getAllByCourseId(any(long.class));
    }

    @Test
    public void CourseItemController_DeleteById_Status200() throws Exception {
        doNothing().when(courseItemService).deleteById(any(long.class));

        MockHttpServletRequestBuilder request = delete(BASE_URL + "/id/" + courseItem.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(courseItemService).deleteById(any(long.class));
    }

    @Test
    public void CourseItemController_DeleteById_Status404() throws Exception {
        long courseItemId = 10;
        doThrow(new CourseItemRuntimeException("Course item not found.")).when(courseItemService).deleteById(any(long.class));

        MockHttpServletRequestBuilder request = delete(BASE_URL + "/id/" + courseItemId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("404 NOT_FOUND \"Course item not found.\"", result.getResolvedException().getMessage()));

        verify(courseItemService).deleteById(any(long.class));
    }
}
