package com.piotrpabich.talktactics.service;

import com.piotrpabich.talktactics.dto.course_item.CourseItemQueryCriteria;
import com.piotrpabich.talktactics.entity.CourseItem;
import com.piotrpabich.talktactics.repository.CourseItemRepository;
import com.piotrpabich.talktactics.repository.FriendInvitationRepository;
import com.piotrpabich.talktactics.repository.UserRepository;
import com.piotrpabich.talktactics.service.course_item.CourseItemServiceImpl;
import com.piotrpabich.talktactics.service.user.UserServiceImpl;
import com.piotrpabich.talktactics.service.user_course.UserCourseServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig
public class CourseItemServiceTests {
    @Mock
    private CourseItemRepository courseItemRepository;
    @Mock
    private FriendInvitationRepository friendInvitationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserServiceImpl userService;
    @Mock
    private UserCourseServiceImpl userCourseService;
    @InjectMocks
    private CourseItemServiceImpl courseItemService;

    private CourseItem courseItem;
    private List<CourseItem> courseItemList;

    @BeforeEach
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, friendInvitationRepository, passwordEncoder, userCourseService);
        courseItemService = new CourseItemServiceImpl(courseItemRepository, userService);
        courseItem = CourseItem.builder()
                .id(1L)
                .word("word")
                .phonetic("phonetic")
                .partOfSpeech("partOfSpeech")
                .build();

        courseItemList = List.of(
                CourseItem.builder()
                        .id(1L)
                        .word("sky")
                        .partOfSpeech("noun")
                        .build(),
                CourseItem.builder()
                        .id(2L)
                        .word("sea")
                        .partOfSpeech("noun")
                        .build(),
                CourseItem.builder()
                        .id(3L)
                        .word("land")
                        .partOfSpeech("noun")
                        .build()
        );

    }

    @Test
    public void Given_QueryAll_When_CourseIdIsNotPassed_Then_ThrowsException() {
        CourseItemQueryCriteria criteria = new CourseItemQueryCriteria();
        criteria.setCourseId(null);
        Pageable pageable = PageRequest.of(0, 3);

        given(courseItemRepository.findAll(any(Specification.class), any(Pageable.class))).willThrow(new IllegalArgumentException("courseId property cannot be null"));

        Assertions.assertThatThrownBy(() -> courseItemService.queryAll(criteria, pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("courseId property cannot be null");
    }
//    @Test
//    public void Given_QueryAll_When_SortingByIdDesc_Then_ReturnsPageResultInDescendingOrder() {
//        given(courseItemRepository.findAll(any(CourseItemQueryCriteria.class), any(Pageable.class)).willReturn(courseItemList));
//
//        Pageable pageable = PageRequest.of(0, 3);
//        PageResult<CourseItemTableRowDto> result = courseItemService.queryAll(new CourseItemQueryCriteria(), pageable);
//    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void CourseItemService_DeleteById_ReturnsVoid() {
        Set<Long> ids = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        courseItemService.delete(ids);
        ids.forEach(id -> verify(courseItemRepository).deleteById(id));
    }
}
