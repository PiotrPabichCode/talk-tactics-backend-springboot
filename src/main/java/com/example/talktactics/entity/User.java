package com.example.talktactics.entity;

import com.example.talktactics.common.CommonEntity;
import com.example.talktactics.dto.user.UserProfilePreviewDto;
import com.example.talktactics.listeners.UserEntityListeners;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EntityListeners(UserEntityListeners.class)
public class User extends CommonEntity implements UserDetails {

    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @Column(unique = true)
    private String email;
    @Size(max = 250)
    private String bio;
    @JsonProperty("total_points")
    private int totalPoints = 0;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<UserCourse> userCourses;

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

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public UserProfilePreviewDto toUserProfilePreviewDto() {
        return UserProfilePreviewDto.builder()
                .id(this.getId())
                .fullName(this.getFullName())
                .totalPoints(this.getTotalPoints())
                .bio(this.getBio())
                .build();
    }
}
