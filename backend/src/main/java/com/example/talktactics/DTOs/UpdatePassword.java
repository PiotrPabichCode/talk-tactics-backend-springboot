package com.example.talktactics.DTOs;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePassword {
    String currentPassword;
    String oldPassword;
    String newPassword;
    String repeatNewPassword;
}
