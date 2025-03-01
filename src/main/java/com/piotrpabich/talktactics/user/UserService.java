package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.user.dto.*;
import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    Page<UserDto> queryAll(UserQueryCriteria criteria, Pageable pageable);
    User getUserByUuid(UUID userUuid, User requester);
    User getUserByUuid(UUID userUuid);
    User getUserByUsername(String username, User requester);
    void deleteUser(UUID userUuid, User requester);
    void updateUser(UUID userUuid, UpdateUserRequest updateUserRequest, User requester);
    void updatePassword(UpdatePasswordRequest request, User requester);
    List<UserProfilePreviewDto> getUserProfiles();
    UserProfileDto getUserProfileByUserUuid(UUID userUuid);
    List<UserProfilePreviewDto> getUserFriends(UUID userUuid, User requester);
    void handleFriendInvitationRequest(FriendInvitationRequest request, User requester);
    void deleteFriend(DeleteFriendRequest request, User requester);
    List<FriendInvitationResponse> getReceivedFriendInvitations(UUID userUuid, Boolean withDetails, User requester);
    List<FriendInvitationResponse> getSentFriendInvitations(UUID userUuid, Boolean withDetails, User requester);

}
