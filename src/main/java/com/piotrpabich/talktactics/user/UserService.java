package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.user.dto.UserDto;
import com.piotrpabich.talktactics.user.dto.UserProfileDto;
import com.piotrpabich.talktactics.user.dto.UserProfilePreviewDto;
import com.piotrpabich.talktactics.user.dto.UserQueryCriteria;
import com.piotrpabich.talktactics.user.dto.DeleteFriendRequest;
import com.piotrpabich.talktactics.user.dto.FriendInvitationRequest;
import com.piotrpabich.talktactics.user.dto.UpdatePasswordRequest;
import com.piotrpabich.talktactics.user.dto.FriendInvitationResponse;
import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService {
    Page<UserDto> queryAll(UserQueryCriteria criteria, Pageable pageable);
    User getUserById(Long id, User requester);
    User getUserById(Long id);
    User getUserByUsername(String username, User requester);
    void deleteUser(Long id, User requester);
    void updateUser(Long id, Map<String, Object> fields, User requester);
    void updatePassword(UpdatePasswordRequest request, User requester);
    List<UserProfilePreviewDto> getUserProfiles();
    UserProfileDto getUserProfileById(Long id);
    List<UserProfilePreviewDto> getFriends(Long id, User requester);
    void handleFriendInvitationRequest(FriendInvitationRequest request, User requester);
    void deleteFriend(DeleteFriendRequest request, User requester);
    List<FriendInvitationResponse> getReceivedFriendInvitations(Long id, Boolean withDetails, User requester);
    List<FriendInvitationResponse> getSentFriendInvitations(Long id, Boolean withDetails, User requester);

}
