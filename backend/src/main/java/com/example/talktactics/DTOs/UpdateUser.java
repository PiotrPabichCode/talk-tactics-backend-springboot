package com.example.talktactics.DTOs;

import com.example.talktactics.models.Role;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {
    Long id;
    String login;
    String oldPassword;
    String password;
    String repeatPassword;
    String firstName;
    String lastName;
    String email;
    String username;
    Role role;
}
