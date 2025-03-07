package com.piotrpabich.talktactics.user.friend;

import com.piotrpabich.talktactics.exception.ConflictException;
import com.piotrpabich.talktactics.user.friend.entity.FriendInvitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendInvitationService {

    private final FriendInvitationRepository friendInvitationRepository;

    public void save(final FriendInvitation friendInvitation) {
        friendInvitationRepository.save(friendInvitation);
    }

    public void existsFriendInvitation(final UUID user1Uuid, final UUID user2Uuid) {
        final var exists = friendInvitationRepository.existsFriendInvitation(user1Uuid, user2Uuid);
        if (exists) {
            throw new ConflictException(String.format("Friend invitation already exists for user1: %s and user2: %s", user1Uuid, user2Uuid));
        }
    }

    public List<FriendInvitation> getFriendInvitations(final Long userId, final FriendInvitationType friendInvitationType) {
        return friendInvitationType == FriendInvitationType.SENT
                ? friendInvitationRepository.findSentFriendInvitationsByUserUuid(userId)
                : friendInvitationRepository.findReceivedFriendInvitationsByUserUuid(userId);
    }

    public void delete(final UUID user1Uuid, final UUID user2Uuid) {
        friendInvitationRepository.findFriendInvitation(user1Uuid, user2Uuid)
                .ifPresent(friendInvitationRepository::delete);
    }
}
