package com.example.talktactics.dto.user.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteFriendDto {
    @JsonProperty("user_id")
    Long userId;
    @JsonProperty("friend_id")
    Long friendId;
}
