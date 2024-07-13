package com.piotrpabich.talktactics.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.dto.user.res.FriendInvitationDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "friend_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class FriendInvitation extends CommonEntity {
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    public FriendInvitationDto toFriendInvitationDto(Boolean withDetails) {
        if(withDetails == null || !withDetails) {
            return new FriendInvitationDto(
                    sender.getId(),
                    receiver.getId(),
                    null,
                    null
            );
        }
        return new FriendInvitationDto(
                sender.getId(),
                receiver.getId(),
                sender.toUserProfilePreviewDto(),
                receiver.toUserProfilePreviewDto()
        );
    }
}
