package com.piotrpabich.talktactics.user.dto;

import com.piotrpabich.talktactics.user.entity.FriendInvitation;
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
