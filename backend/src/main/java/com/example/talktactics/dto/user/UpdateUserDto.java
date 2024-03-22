package com.example.talktactics.dto.user;

import com.example.talktactics.entity.Role;
import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    Long id;
    String username;
    String firstName;
    String lastName;
    String email;
    Role role;
}
