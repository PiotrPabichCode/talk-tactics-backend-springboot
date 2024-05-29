package com.example.talktactics.dto.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.talktactics.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    @JSONField(name = "first_name")
    private String firstName;
    @JSONField(name = "last_name")
    private String lastName;
    private String email;
    private String bio;
    @JSONField(name = "total_points")
    private Integer totalPoints;
    private Role role;
}
