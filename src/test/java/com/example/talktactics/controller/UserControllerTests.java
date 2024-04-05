package com.example.talktactics.controller;

import com.example.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.example.talktactics.entity.Role;
import com.example.talktactics.entity.User;
import com.example.talktactics.exception.UserRuntimeException;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    private static final String BASE_URL = "/api/v1/users";

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtServiceImpl jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private User updatedUser;
    private List<User> userList;
    private UpdatePasswordReqDto updatePasswordReqDto;

    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1)
                .email("wayne_rooney@email.com")
                .firstName("Wayne")
                .lastName("Rooney")
                .password("pass1!@#")
                .role(Role.ADMIN)
                .username("wayne_rooney")
                .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                .build();

        updatedUser = User.builder()
                .id(1)
                .email("wayne_rooney@email.com")
                .firstName("Michael")
                .lastName("Phelps")
                .role(Role.ADMIN)
                .username("wayne_rooney")
                .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                .build();

        userList = List.of(
                User.builder()
                        .id(1)
                        .email("wayne_rooney@gmail.com")
                        .firstName("Wayne")
                        .lastName("Rooney")
                        .role(Role.ADMIN)
                        .username("wayne_rooney")
                        .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                        .build(),
                User.builder()
                        .id(2)
                        .email("dwayne_johnson@gmail.com")
                        .firstName("Dwayne")
                        .lastName("Johnson")
                        .role(Role.USER)
                        .username("dwayne_johnson")
                        .bio("Devoted language aficionado committed to unraveling the intricacies of English fluency, one word at a time. Embark on a voyage of linguistic discovery and expertise with me!")
                        .build()
        );

        updatePasswordReqDto = UpdatePasswordReqDto.builder()
                .id(1L)
                .oldPassword("pass1!@#")
                .newPassword("new1!@#")
                .repeatNewPassword("new1!@#")
                .build();
    }

    @Test
    public void UserController_CreateUserStatus200() throws Exception {
        given(userService.createUser(any(User.class))).willReturn(user);

        MockHttpServletRequestBuilder request = post(BASE_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));

        verify(userService).createUser(any(User.class));
    }

    @Test
    public void UserController_CreateUserStatus422() throws Exception {

        given(userService.createUser(any(User.class))).willThrow(new UserRuntimeException("Custom error message"));

        MockHttpServletRequestBuilder request = post(BASE_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("422 UNPROCESSABLE_ENTITY \"Custom error message\"", result.getResolvedException().getMessage()));

        verify(userService).createUser(any(User.class));
    }

    @Test
    public void UserController_GetAllUsersStatus200() throws Exception {
        given(userService.getUsers()).willReturn(userList);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(userList.size()))
                .andExpect(jsonPath("$[0].id").value(userList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(userList.get(1).getId()));

        verify(userService).getUsers();
    }

    @Test
    public void UserController_GetAllUsersStatus404() throws Exception {
        given(userService.getUsers()).willThrow(new UserRuntimeException("No users found"));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/all")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"No users found\"", result.getResolvedException().getMessage()));

        verify(userService).getUsers();
    }

    @Test
    public void UserController_GetUserByIdStatus200() throws Exception {
        long userId = 2;
        User returnedUser = userList.get(1);
        given(userService.getUserById(any(long.class))).willReturn(returnedUser);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(returnedUser.getEmail()))
                .andExpect(jsonPath("$.username").value(returnedUser.getUsername()));

        verify(userService).getUserById(any(long.class));
    }

    @Test
    public void UserController_GetUserByIdStatus404() throws Exception {
        long userId = 2;
        given(userService.getUserById(any(long.class))).willThrow(new UserRuntimeException("User not found"));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"User not found\"", result.getResolvedException().getMessage()));

        verify(userService).getUserById(any(long.class));
    }

    @Test
    public void UserController_GetUserByUsernameStatus200() throws Exception {
        String username = "wayne_rooney";
        given(userService.getUserByUsername(any(String.class))).willReturn(user);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/username/" + username)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("wayne_rooney@email.com"))
                .andExpect(jsonPath("$.username").value("wayne_rooney"));

        verify(userService).getUserByUsername(any(String.class));
    }

    @Test
    public void UserController_GetUserByUsernameStatus404() throws Exception {
        String username = "wayne_rooney";
        given(userService.getUserByUsername(any(String.class))).willThrow(new UserRuntimeException("User not found"));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/username/" + username)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"User not found\"", result.getResolvedException().getMessage()));

        verify(userService).getUserByUsername(any(String.class));
    }

    @Test
    public void UserController_UpdateUserStatus200() throws Exception {
        long userId = 1L;
        Map<String, Object> fields = new HashMap<>();
        fields.put("first_name", "Michael");
        fields.put("last_name", "Phelps");

        given(userService.updateUser(anyLong(), anyMap())).willReturn(updatedUser);

        MockHttpServletRequestBuilder request = patch(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fields));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value(updatedUser.getFirstName()))
                .andExpect(jsonPath("$.last_name").value(updatedUser.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.username").value(updatedUser.getUsername()));

        verify(userService).updateUser(anyLong(), anyMap());
    }

    @Test
    public void UserController_UpdateUserStatus400() throws Exception {
        long userId = 1L;
        Map<String, Object> fields = new HashMap<>();
        fields.put("first_name1", "Michael");
        fields.put("last_name", "Phelps");

        given(userService.updateUser(userId, fields)).willThrow(new UserRuntimeException("Invalid fields"));

        MockHttpServletRequestBuilder request = patch(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fields));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("400 BAD_REQUEST \"Invalid fields\"", result.getResolvedException().getMessage()));

        verify(userService).updateUser(userId, fields);
    }

    @Test
    public void UserController_DeleteUserStatus200() throws Exception {
        long userId = 1;

        doNothing().when(userService).deleteUser(any(long.class));

        MockHttpServletRequestBuilder request = delete(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).deleteUser(any(long.class));
    }

    @Test
    public void UserController_DeleteUserStatus404() throws Exception {
        long userId = 1;

        doThrow(new UserRuntimeException("User not found")).when(userService).deleteUser(any(long.class));

        MockHttpServletRequestBuilder request = delete(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("404 NOT_FOUND \"User not found\"", result.getResolvedException().getMessage()));

        verify(userService).deleteUser(any(long.class));
    }

    @Test
    public void UserController_UpdatePasswordStatus200() throws Exception {
        given(userService.updatePassword(updatePasswordReqDto)).willReturn(user);

        MockHttpServletRequestBuilder request = put(BASE_URL + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordReqDto));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()));

        verify(userService).updatePassword(updatePasswordReqDto);
    }

    @Test
    public void UserController_UpdatePasswordStatus403() throws Exception {

        given(userService.updatePassword(updatePasswordReqDto)).willThrow(new UserRuntimeException("Invalid password"));

        MockHttpServletRequestBuilder request = put(BASE_URL + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordReqDto));

        mockMvc.perform(request)
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andExpect(result -> assertEquals("403 FORBIDDEN \"Invalid password\"", result.getResolvedException().getMessage()));

        verify(userService).updatePassword(updatePasswordReqDto);
    }
}