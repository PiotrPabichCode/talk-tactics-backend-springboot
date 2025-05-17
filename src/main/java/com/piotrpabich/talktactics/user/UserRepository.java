package com.piotrpabich.talktactics.user;

import com.piotrpabich.talktactics.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUuid(UUID userUuid);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.friends WHERE u.uuid = :userUuid")
    Optional<User> findByUuidWithFriends(UUID userUuid);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.courseParticipants WHERE u.uuid = :userUuid")
    Optional<User> findByUuidWithCourses(UUID userUuid);
}