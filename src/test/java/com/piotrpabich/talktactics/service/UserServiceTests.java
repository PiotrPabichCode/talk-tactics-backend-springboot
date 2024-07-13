package com.piotrpabich.talktactics.service;

import com.piotrpabich.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.piotrpabich.talktactics.entity.Role;
import com.piotrpabich.talktactics.entity.User;
import com.piotrpabich.talktactics.repository.FriendInvitationRepository;
import com.piotrpabich.talktactics.repository.UserRepository;
import com.piotrpabich.talktactics.service.user.UserServiceImpl;
import com.piotrpabich.talktactics.service.user_course.UserCourseServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@SpringJUnitConfig
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FriendInvitationRepository friendInvitationRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCourseServiceImpl userCourseService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    private User user;
    private List<User> userList;
    private UpdatePasswordReqDto updatePasswordReqDto;

    @BeforeEach
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();

        userServiceImpl = new UserServiceImpl(userRepository, friendInvitationRepository, passwordEncoder, userCourseService);

        user = User.builder()
                .email("dwayne_johnson@gmail.com")
                .firstName("Dwayne")
                .lastName("Johnson")
                .password(passwordEncoder.encode("pass22!@#"))
                .role(Role.USER)
                .username("dwayne_johnson")
                .bio("Devoted language aficionado committed to unraveling the intricacies of English fluency, one word at a time. Embark on a voyage of linguistic discovery and expertise with me!")
                .build();

        userList = List.of(
                User.builder()
                        .email("wayne_rooney@gmail.com")
                        .firstName("Wayne")
                        .lastName("Rooney")
                        .password(passwordEncoder.encode("pass11!@#"))
                        .role(Role.ADMIN)
                        .username("wayne_rooney")
                        .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                        .build(),
                User.builder()
                        .email("dwayne_johnson@gmail.com")
                        .firstName("Dwayne")
                        .lastName("Johnson")
                        .password(passwordEncoder.encode("pass22!@#"))
                        .role(Role.USER)
                        .username("dwayne_johnson")
                        .bio("Devoted language aficionado committed to unraveling the intricacies of English fluency, one word at a time. Embark on a voyage of linguistic discovery and expertise with me!")
                        .build()
        );

        updatePasswordReqDto = new UpdatePasswordReqDto(
                1L,
                "pass11!@#",
                "pass12!@#",
                "pass12!@#"
        );
    }
    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserService_GetUserById_ReturnsUser() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        User foundUser = userServiceImpl.getUserById(1);

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getUsername()).isEqualTo("dwayne_johnson");
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserService_GetUserByUsername_ReturnsUser() {
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));

        User foundUser = userServiceImpl.getUserByUsername("dwayne_johnson");

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getUsername()).isEqualTo("dwayne_johnson");
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void UserService_DeleteUser_ReturnsVoid() {
        given(userRepository.existsById(anyLong())).willReturn(true);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        willDoNothing().given(userRepository).deleteById(anyLong());

        userServiceImpl.deleteUser(1);

        // No exception thrown
        // If an exception is thrown, the test will fail
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserService_UpdateUser_ReturnsUser() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);

        Map<String, Object> fields = Map.of("first_name", "Tom", "last_name", "Hanks");

        User updatedUser = userServiceImpl.updateUser(1, fields);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getFirstName()).isEqualTo("Tom");
        Assertions.assertThat(updatedUser.getLastName()).isEqualTo("Hanks");
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserService_UpdatePassword_ReturnsUser() {
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);

        User updatedUser = userServiceImpl.updatePassword(updatePasswordReqDto);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(passwordEncoder.matches(updatePasswordReqDto.newPassword(), updatedUser.getPassword())).isTrue();
    }
}
