package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.user.entity.FriendInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendInvitationRepository extends JpaRepository<FriendInvitation, Long> {
    @Query("SELECT fi FROM FriendInvitation fi " +
            "WHERE (fi.sender.id = :user1Id AND fi.receiver.id = :user2Id) " +
            "OR (fi.sender.id = :user2Id AND fi.receiver.id = :user1Id)")
    Optional<FriendInvitation> findFriendInvitationByUserIds(@Param("user1Id") Long user1Id,
                                                             @Param("user2Id") Long user2Id);

    @Query("SELECT CASE WHEN COUNT(fi) > 0 THEN true ELSE false END " +
            "FROM FriendInvitation fi " +
            "WHERE (fi.sender.id = :user1Id AND fi.receiver.id = :user2Id) " +
            "OR (fi.sender.id = :user2Id AND fi.receiver.id = :user1Id)")
    boolean existsFriendInvitationByUserIds(@Param("user1Id") Long user1Id,
                                            @Param("user2Id") Long user2Id);
}