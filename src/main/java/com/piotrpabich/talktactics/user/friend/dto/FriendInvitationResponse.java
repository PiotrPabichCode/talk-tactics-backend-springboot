package com.piotrpabich.talktactics.user.friend.dto;

import com.piotrpabich.talktactics.user.dto.UserProfilePreviewDto;
import com.piotrpabich.talktactics.user.friend.entity.FriendInvitation;
import jakarta.validation.constraints.NotNull;

public record FriendInvitationResponse(
        @NotNull
        UserProfilePreviewDto sender,
        @NotNull
        UserProfilePreviewDto receiver
) {
    public static FriendInvitationResponse of(final FriendInvitation friendInvitation) {
        return new FriendInvitationResponse(
                UserProfilePreviewDto.of(friendInvitation.getSender()),
                UserProfilePreviewDto.of(friendInvitation.getReceiver())
        );
    }
}
