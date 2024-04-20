package com.example.talktactics.listeners;

import com.example.talktactics.entity.User;
import jakarta.persistence.PreUpdate;

public class UserEntityListeners {
    @PreUpdate
    public void beforeUpdate(User user) {
    }
}
