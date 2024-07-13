package com.piotrpabich.talktactics.controller;

import com.piotrpabich.talktactics.config.CorsConfig;
import com.piotrpabich.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.piotrpabich.talktactics.entity.Role;
import com.piotrpabich.talktactics.entity.User;
import com.piotrpabich.talktactics.exception.BadRequestException;
import com.piotrpabich.talktactics.exception.EntityNotFoundException;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    private static final String BASE_URL = "/api/v1/users";

    @MockBean
    private CorsConfig corsConfig;
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
                .id(1L)
                .email("wayne_rooney@email.com")
                .firstName("Wayne")
                .lastName("Rooney")
                .password("pass1!@#")
                .role(Role.ADMIN)
                .username("wayne_rooney")
                .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                .build();

        updatedUser = User.builder()
                .id(1L)
                .email("wayne_rooney@email.com")
                .firstName("Michael")
                .lastName("Phelps")
                .role(Role.ADMIN)
                .username("wayne_rooney")
                .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                .build();

        userList = List.of(
                User.builder()
                        .id(1L)
                        .email("wayne_rooney@gmail.com")
                        .firstName("Wayne")
                        .lastName("Rooney")
                        .role(Role.ADMIN)
                        .username("wayne_rooney")
                        .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                        .build(),
                User.builder()
                        .id(2L)
                        .email("dwayne_johnson@gmail.com")
                        .firstName("Dwayne")
                        .lastName("Johnson")
                        .role(Role.USER)
                        .username("dwayne_johnson")
                        .bio("Devoted language aficionado committed to unraveling the intricacies of English fluency, one word at a time. Embark on a voyage of linguistic discovery and expertise with me!")
                        .build()
        );

        updatePasswordReqDto = new UpdatePasswordReqDto(
                1L,
                "pass1!@#",
                "new1!@#",
                "new1!@#"
        );
    }

    @Test
    public void UserController_GetUserByIdStatus200() throws Exception {
        long userId = 2;
        User returnedUser = userList.get(1);
        given(userService.getUserById(anyLong())).willReturn(returnedUser);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(returnedUser.getEmail()))
                .andExpect(jsonPath("$.username").value(returnedUser.getUsername()));

        verify(userService).getUserById(anyLong());
    }

    @Test
    public void UserController_GetUserByIdStatus404() throws Exception {
        long userId = 2;
        given(userService.getUserById(anyLong())).willThrow(new EntityNotFoundException(User.class, "id", String.valueOf(userId)));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(String.format("User with id %d does not exist", userId), result.getResolvedException().getMessage()));

        verify(userService).getUserById(anyLong());
    }

    @Test
    public void UserController_GetUserByUsernameStatus200() throws Exception {
        String username = "wayne_rooney";
        given(userService.getUserByUsername(anyString())).willReturn(user);

        MockHttpServletRequestBuilder request = get(BASE_URL + "/username/" + username)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("wayne_rooney@email.com"))
                .andExpect(jsonPath("$.username").value("wayne_rooney"));

        verify(userService).getUserByUsername(any(String.class));
    }

    @Test
    public void UserController_GetUserByUsernameStatus404() throws Exception {
        String username = "wayne_rooney";
        given(userService.getUserByUsername(anyString())).willThrow(new EntityNotFoundException(User.class, "username", username));

        MockHttpServletRequestBuilder request = get(BASE_URL + "/username/" + username)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(String.format("User with username %s does not exist", username), result.getResolvedException().getMessage()));

        verify(userService).getUserByUsername(any(String.class));
    }

    @Test
    public void UserController_UpdateUserStatus204() throws Exception {
        long userId = 1L;
        Map<String, Object> fields = new HashMap<>();
        fields.put("first_name", "Michael");
        fields.put("last_name", "Phelps");

        given(userService.updateUser(anyLong(), anyMap())).willReturn(updatedUser);

        MockHttpServletRequestBuilder request = patch(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fields));

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(userService).updateUser(anyLong(), anyMap());
    }

    @Test
    public void UserController_UpdateUserStatus400() throws Exception {
        long userId = 1L;
        Map<String, Object> fields = new HashMap<>();
        fields.put("first_name1", "Michael");
        fields.put("last_name", "Phelps");

        given(userService.updateUser(userId, fields)).willThrow(new BadRequestException("Invalid fields"));

        MockHttpServletRequestBuilder request = patch(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fields));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals("Invalid fields", result.getResolvedException().getMessage()));

        verify(userService).updateUser(userId, fields);
    }

    @Test
    public void UserController_DeleteUserStatus204() throws Exception {
        long userId = 1;

        doNothing().when(userService).deleteUser(anyLong());

        MockHttpServletRequestBuilder request = delete(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(anyLong());
    }

    @Test
    public void UserController_DeleteUserStatus404() throws Exception {
        long userId = 1;

        doThrow(new EntityNotFoundException(User.class, "id", String.valueOf(userId))).when(userService).deleteUser(anyLong());

        MockHttpServletRequestBuilder request = delete(BASE_URL + "/id/" + userId)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(String.format("User with id %d does not exist", userId), result.getResolvedException().getMessage()));

        verify(userService).deleteUser(anyLong());
    }

    @Test
    public void UserController_UpdatePasswordStatus204() throws Exception {
        given(userService.updatePassword(any(UpdatePasswordReqDto.class))).willReturn(user);

        MockHttpServletRequestBuilder request = put(BASE_URL + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordReqDto));

        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(userService).updatePassword(updatePasswordReqDto);
    }

    @Test
    public void UserController_UpdatePasswordStatus400() throws Exception {

        given(userService.updatePassword(any(UpdatePasswordReqDto.class))).willThrow(new BadRequestException("Invalid password"));

        MockHttpServletRequestBuilder request = put(BASE_URL + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordReqDto));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals("Invalid password", result.getResolvedException().getMessage()));

        verify(userService).updatePassword(updatePasswordReqDto);
    }
}