package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.user.dto.UserDto;
import com.piotrpabich.talktactics.user.dto.UserProfileDto;
import com.piotrpabich.talktactics.user.dto.UserProfilePreviewDto;
import com.piotrpabich.talktactics.user.dto.UserQueryCriteria;
import com.piotrpabich.talktactics.user.dto.req.DeleteFriendDto;
import com.piotrpabich.talktactics.user.dto.req.FriendInvitationRequest;
import com.piotrpabich.talktactics.user.dto.req.UpdatePasswordReqDto;
import com.piotrpabich.talktactics.user.dto.res.FriendInvitationDto;
import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService {
    PageResult<UserDto> queryAll(UserQueryCriteria criteria, Pageable pageable);
    User getUserById(long id, User requester);
    User getUserById(long id);
    User getUserByUsername(String username, User requester);
    void deleteUser(long id, User requester);
    User updateUser(long id, Map<String, Object> fields, User requester);
    User updatePassword(UpdatePasswordReqDto req, User requester);
    List<UserProfilePreviewDto> getUserProfiles();
    UserProfileDto getUserProfileById(Long id);
    List<UserProfilePreviewDto> getFriends(Long id, User requester);
    void handleFriendInvitationRequest(FriendInvitationRequest request, User requester);
    void deleteFriend(DeleteFriendDto request, User requester);
    List<FriendInvitationDto> getReceivedFriendInvitations(Long id, Boolean withDetails, User requester);
    List<FriendInvitationDto> getSentFriendInvitations(Long id, Boolean withDetails, User requester);

}
