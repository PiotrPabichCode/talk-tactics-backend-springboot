package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.user.entity.FriendInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendInvitationRepository extends JpaRepository<FriendInvitation, Long> {
    @Query("SELECT fi FROM FriendInvitation fi " +
            "WHERE (fi.sender.uuid = :user1Uuid AND fi.receiver.uuid = :user2Uuid) " +
            "OR (fi.sender.uuid = :user2Uuid AND fi.receiver.uuid = :user1Uuid)")
    Optional<FriendInvitation> findFriendInvitation(UUID user1Uuid, UUID user2Uuid);

    @Query("SELECT COUNT(fi) > 0 FROM FriendInvitation fi " +
            "WHERE (fi.sender.uuid = :user1Uuid AND fi.receiver.uuid = :user2Uuid) " +
            "OR (fi.sender.uuid = :user2Uuid AND fi.receiver.uuid = :user1Uuid)")
    boolean existsFriendInvitation(UUID user1Uuid, UUID user2Uuid);

    @Query("SELECT fi FROM FriendInvitation fi WHERE fi.sender.id = :userId ORDER BY fi.createdAt DESC")
    List<FriendInvitation> findSentFriendInvitationsByUserUuid(Long userId);

    @Query("SELECT fi FROM FriendInvitation fi WHERE fi.receiver.id = :userId ORDER BY fi.createdAt DESC")
    List<FriendInvitation> findReceivedFriendInvitationsByUserUuid(Long userId);

}