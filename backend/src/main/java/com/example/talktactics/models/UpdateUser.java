package com.example.talktactics.models;

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
