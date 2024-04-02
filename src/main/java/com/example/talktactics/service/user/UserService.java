package com.example.talktactics.service.user;

import com.example.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.example.talktactics.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User createUser(User user);
    List<User> getUsers();
    User getUserById(Long id);
    User getUserByUsername(String username);
    void deleteUser(Long id);
    User updateUser(Long id, Map<String, Object> fields);
    void validateCredentials(User user);
    void validateFields(Map<String, Object> fields);
    User updatePassword(UpdatePasswordReqDto req);
}
