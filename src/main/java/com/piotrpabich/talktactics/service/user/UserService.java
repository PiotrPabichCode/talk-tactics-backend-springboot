package com.piotrpabich.talktactics.service.user;

import com.piotrpabich.talktactics.common.PageResult;
import com.piotrpabich.talktactics.dto.user.*;
import com.piotrpabich.talktactics.dto.user.req.DeleteFriendDto;
import com.piotrpabich.talktactics.dto.user.req.FriendInvitationRequest;
import com.piotrpabich.talktactics.dto.user.req.UpdatePasswordReqDto;
import com.piotrpabich.talktactics.dto.user.res.FriendInvitationDto;
import com.piotrpabich.talktactics.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UserService {
    PageResult<UserDto> queryAll(UserQueryCriteria criteria, Pageable pageable);
    User getUserById(long id);
    User getUserByUsername(String username);
    void deleteUser(long id);
    User updateUser(long id, Map<String, Object> fields);
    void validateCredentials(User user);
    void validateAdmin();
    User updatePassword(UpdatePasswordReqDto req);
    List<UserProfilePreviewDto> getUserProfiles();
    UserProfileDto getUserProfileById(Long id);
    List<UserProfilePreviewDto> getFriends(Long id);
    void handleFriendInvitationRequest(FriendInvitationRequest request);
    void deleteFriend(DeleteFriendDto request);
    List<FriendInvitationDto> getReceivedFriendInvitations(Long id, Boolean withDetails);
    List<FriendInvitationDto> getSentFriendInvitations(Long id, Boolean withDetails);

}
