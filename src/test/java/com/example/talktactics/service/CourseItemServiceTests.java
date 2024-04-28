package com.example.talktactics.service;

import com.example.talktactics.dto.course_item.CourseItemPreviewDto;
import com.example.talktactics.entity.Course;
import com.example.talktactics.entity.CourseItem;
import com.example.talktactics.repository.CourseItemRepository;
import com.example.talktactics.repository.FriendInvitationRepository;
import com.example.talktactics.repository.UserRepository;
import com.example.talktactics.service.course.CourseServiceImpl;
import com.example.talktactics.service.course_item.CourseItemServiceImpl;
import com.example.talktactics.service.user.UserServiceImpl;
import com.example.talktactics.service.user_course.UserCourseService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
    private UserCourseService userCourseService;
    @InjectMocks
    private CourseItemServiceImpl courseItemService;

    private CourseItem courseItem;
    private List<CourseItem> courseItemList;
    private List<CourseItemPreviewDto> courseItemPreviewDtoList;

    @BeforeEach
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, friendInvitationRepository, userCourseService, passwordEncoder);
        courseItemService = new CourseItemServiceImpl(courseItemRepository, userService);
        courseItem = CourseItem.builder()
                .id(1L)
                .word("word")
                .phonetic("phonetic")
                .partOfSpeech("partOfSpeech")
                .build();

        courseItemList = List.of(
                CourseItem.builder()
                        .id(1)
                        .course(Course.builder().id(1).build())
                        .word("word")
                        .phonetic("phonetic")
                        .partOfSpeech("partOfSpeech")
                        .build(),
                CourseItem.builder()
                        .id(2)
                        .course(Course.builder().id(2).build())
                        .word("word2")
                        .phonetic("phonetic2")
                        .partOfSpeech("partOfSpeech2")
                        .build()
        );

        courseItemPreviewDtoList = courseItemList.stream().map(CourseItem::toDTO).toList();
    }

    @Test
    public void CourseItemService_GetAll_ReturnsCourseItemPreviewDtoList() {
        given(courseItemRepository.findAll()).willReturn(courseItemList);

        List<CourseItemPreviewDto> result = courseItemService.getAll();

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(courseItemPreviewDtoList.size());
        Assertions.assertThat(result.get(0).getCourseName()).isEqualTo(courseItemPreviewDtoList.get(0).getCourseName());
    }

    @Test
    public void CourseItemService_GetAllByCourseId_ReturnsCourseItemPreviewDtoList() {
        given(courseItemRepository.findByCourseId(any(long.class))).willReturn(List.of(courseItemList.get(0)));

        List<CourseItemPreviewDto> result = courseItemService.getAllByCourseId(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getCourseName()).isEqualTo(courseItemPreviewDtoList.get(0).getCourseName());
    }

    @Test
    public void CourseItemService_FindById_ReturnsCourseItem() {
        given(courseItemRepository.findById(any(long.class))).willReturn(Optional.of(courseItem));

        CourseItem result = courseItemService.findById(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getWord()).isEqualTo(courseItem.getWord());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void CourseItemService_DeleteById_ReturnsVoid() {
        given(courseItemRepository.existsById(any(long.class))).willReturn(true);

        courseItemService.deleteById(1);
    }
}
