package com.example.talktactics.dto.user.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendInvitationRequest {
    @JsonProperty("sender_id")
    Long senderId;
    @JsonProperty("receiver_id")
    Long receiverId;
}
