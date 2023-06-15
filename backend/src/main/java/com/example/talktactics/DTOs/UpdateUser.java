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
    String firstName;
    String lastName;
    String email;
    Role role;
}
