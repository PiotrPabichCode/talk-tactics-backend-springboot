package com.example.talktactics.service;

import com.example.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.example.talktactics.entity.Role;
import com.example.talktactics.entity.User;
import com.example.talktactics.repository.UserRepository;
import com.example.talktactics.service.user.UserServiceImpl;
import com.example.talktactics.service.user_course.UserCourseService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@SpringJUnitConfig
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserCourseService userCourseService;

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    private User user;
    private List<User> userList;
    private UpdatePasswordReqDto updatePasswordReqDto;

    @BeforeEach
    public void init() {
        passwordEncoder = new BCryptPasswordEncoder();

        userServiceImpl = new UserServiceImpl(userRepository, userCourseService, passwordEncoder);

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

        updatePasswordReqDto = UpdatePasswordReqDto.builder()
                .id(1L)
                .oldPassword("pass11!@#")
                .newPassword("pass12!@#")
                .repeatNewPassword("pass12!@#")
                .build();
    }

    @Test
    @WithAnonymousUser
    public void UserService_CreateUser_ReturnsUser() {
        given(userRepository.save(any(User.class))).willReturn(user);

        User createdUser = userServiceImpl.createUser(user);

        Assertions.assertThat(createdUser).isNotNull();
        Assertions.assertThat(createdUser.getUsername()).isEqualTo("dwayne_johnson");
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void UserService_GetUsers_ReturnsListOfUsers() {
        given(userRepository.findAll()).willReturn(userList);

        List<User> users = userServiceImpl.getUsers();

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserService_GetUserById_ReturnsUser() {
        given(userRepository.findById(any(long.class))).willReturn(Optional.of(user));

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
        given(userRepository.existsById(any(long.class))).willReturn(true);
        given(userRepository.findById(any(long.class))).willReturn(Optional.of(user));
        willDoNothing().given(userRepository).deleteById(any(long.class));

        userServiceImpl.deleteUser(1);

        // No exception thrown
        // If an exception is thrown, the test will fail
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserService_UpdateUser_ReturnsUser() {
        given(userRepository.findById(any(long.class))).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);

        Map<String, Object> fields = Map.of("first_name", "Tom", "last_name", "Hanks");

        User updatedUser = userServiceImpl.updateUser(1, fields);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getFirstName()).isEqualTo("Tom");
        Assertions.assertThat(updatedUser.getLastName()).isEqualTo("Hanks");
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void UserService_ValidateAdmin_ReturnsVoid() {
        userServiceImpl.validateAdmin();
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserService_ValidateCredentials_ReturnsVoid() {
        userServiceImpl.validateCredentials(user);
    }

    @Test
    @WithMockUser(username = "dwayne_johnson")
    public void UserService_UpdatePassword_ReturnsUser() {
        given(userRepository.findById(any(long.class))).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user);

        User updatedUser = userServiceImpl.updatePassword(updatePasswordReqDto);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(passwordEncoder.matches(updatePasswordReqDto.getNewPassword(), updatedUser.getPassword())).isTrue();
    }
}
