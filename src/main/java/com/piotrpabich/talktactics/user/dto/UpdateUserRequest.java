package com.piotrpabich.talktactics.user.dto;

import com.piotrpabich.talktactics.user.entity.User;

import java.util.Optional;

public record UpdateUserRequest(
        String username,
        String firstName,
        String lastName,
        String email,
        String bio
) {

    public static User of(final User user, final UpdateUserRequest updateUserRequest) {
        Optional.ofNullable(updateUserRequest.username()).ifPresent(user::setUsername);
        Optional.ofNullable(updateUserRequest.email()).ifPresent(user::setEmail);
        Optional.ofNullable(updateUserRequest.firstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(updateUserRequest.lastName()).ifPresent(user::setLastName);
        Optional.ofNullable(updateUserRequest.bio()).ifPresent(user::setBio);

        return user;
    }
}
