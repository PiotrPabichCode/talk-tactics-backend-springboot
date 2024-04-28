package com.example.talktactics.dto.user.req;

import com.example.talktactics.dto.user.UserProfilePreviewDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestDto {
    @JsonProperty("sender_id")
    Long senderId;
    @JsonProperty("receiver_id")
    Long receiverId;

    @JsonInclude(Include.NON_NULL)
    UserProfilePreviewDto sender;
    @JsonInclude(Include.NON_NULL)
    UserProfilePreviewDto receiver;
}
