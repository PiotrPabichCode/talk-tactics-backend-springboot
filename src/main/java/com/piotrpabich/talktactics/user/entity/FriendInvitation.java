package com.piotrpabich.talktactics.user.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.user.dto.FriendInvitationResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "friend_requests")
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
public class FriendInvitation extends CommonEntity {
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    public FriendInvitationResponse toFriendInvitationDto(Boolean withDetails) {
        if(withDetails == null || !withDetails) {
            return new FriendInvitationResponse(
                    sender.getId(),
                    receiver.getId(),
                    null,
                    null
            );
        }
        return new FriendInvitationResponse(
                sender.getId(),
                receiver.getId(),
                sender.toUserProfilePreviewDto(),
                receiver.toUserProfilePreviewDto()
        );
    }
}
