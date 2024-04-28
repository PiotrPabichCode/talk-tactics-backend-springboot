package com.example.talktactics.service.user;

import com.example.talktactics.dto.user.UserProfileDto;
import com.example.talktactics.dto.user.UserProfilePreviewDto;
import com.example.talktactics.dto.user.req.DeleteFriendDto;
import com.example.talktactics.dto.user.req.FriendInvitationRequest;
import com.example.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.example.talktactics.dto.user.res.FriendInvitationDto;
import com.example.talktactics.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    User createUser(User user);
    List<User> getUsers();
    User getUserById(long id);
    User getUserByUsername(String username);
    void deleteUser(long id);
    User updateUser(long id, Map<String, Object> fields);
    void validateCredentials(User user);
    void validateAdmin();
    void validateFields(Map<String, Object> fields);
    User updatePassword(UpdatePasswordReqDto req);
    List<UserProfilePreviewDto> getUserProfiles();
    UserProfileDto getUserProfileById(Long id);
    List<UserProfilePreviewDto> getFriends(Long id);
    void sendFriendInvitation(FriendInvitationRequest request);
    void acceptFriendInvitation(FriendInvitationRequest request);
    void rejectFriendInvitation(FriendInvitationRequest request);
    void deleteSentFriendInvitation(FriendInvitationRequest request);
    void deleteFriend(DeleteFriendDto request);
    List<FriendInvitationDto> getReceivedFriendInvitations(Long id, Boolean withDetails);
    List<FriendInvitationDto> getSentFriendInvitations(Long id, Boolean withDetails);
}
