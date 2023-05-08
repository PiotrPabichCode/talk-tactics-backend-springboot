package com.example.talktactics.dto;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    @NotBlank(message = "Login is required")
    String login;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String repeatPassword;
}
