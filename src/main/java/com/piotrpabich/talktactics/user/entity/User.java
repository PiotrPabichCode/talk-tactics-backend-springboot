package com.piotrpabich.talktactics.user.entity;

import com.piotrpabich.talktactics.common.CommonEntity;
import com.piotrpabich.talktactics.user.dto.UserProfilePreviewDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.piotrpabich.talktactics.user_course.entity.UserCourse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
@SuperBuilder(toBuilder = true)
public class User extends CommonEntity implements UserDetails {

    @Column(unique = true)
    private String username;

    @JsonIgnore
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

    @JsonIgnore
    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<UserCourse> userCourses = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender",
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @OrderBy("createdAt DESC")
    private List<FriendInvitation> sentFriendInvitations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiver",
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @OrderBy("createdAt DESC")
    private List<FriendInvitation> receivedFriendInvitations = new ArrayList<>();

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserProfilePreviewDto toUserProfilePreviewDto() {
        return new UserProfilePreviewDto(
                this.getId(),
                this.getFirstName(),
                this.getLastName(),
                this.getTotalPoints(),
                this.getBio()
        );
    }

    public boolean isAdmin() {
        return this.role.equals(Role.ADMIN);
    }
}
