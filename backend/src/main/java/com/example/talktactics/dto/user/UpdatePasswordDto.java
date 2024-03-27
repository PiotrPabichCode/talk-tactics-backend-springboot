package com.example.talktactics.dto.user;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDto {
    String currentPassword;
    String oldPassword;
    String newPassword;
    String repeatNewPassword;
}
