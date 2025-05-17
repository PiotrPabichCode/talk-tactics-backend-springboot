package com.piotrpabich.talktactics.user.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.course.participant.entity.CourseParticipant;
import com.piotrpabich.talktactics.user.friend.entity.FriendInvitation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
public class User extends CommonEntity implements UserDetails {

    private UUID uuid = UUID.randomUUID();

    @Column(unique = true)
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    @Email(message = "Invalid email address", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Column(unique = true)
    private String email;

    private String bio;

    private int totalPoints;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<CourseParticipant> courseParticipants = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends = new ArrayList<>();

    @OneToMany(mappedBy = "sender", orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<FriendInvitation> sentFriendInvitations = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<FriendInvitation> receivedFriendInvitations = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {
        return this.role.equals(Role.ADMIN);
    }

    public void addFriend(final User user) {
        if (user != null && !this.equals(user) && !this.friends.contains(user)) {
            this.getFriends().add(user);
            user.getFriends().add(this);
        }
    }

    public void removeFriend(final User user) {
        if (user != null && this.friends.contains(user)) {
            this.getFriends().remove(user);
            user.getFriends().remove(this);
        }
    }

    public void addPoints(final int points) {
        this.totalPoints += points;
    }
}
