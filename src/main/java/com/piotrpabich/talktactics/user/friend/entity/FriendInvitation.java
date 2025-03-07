package com.piotrpabich.talktactics.user.friend.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend_requests")
@Getter
@Setter
@RequiredArgsConstructor
public class FriendInvitation extends CommonEntity {

    public FriendInvitation(final User sender, final User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
}
