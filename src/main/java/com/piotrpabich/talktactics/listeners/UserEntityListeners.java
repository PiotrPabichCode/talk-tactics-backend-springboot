package com.piotrpabich.talktactics.listeners;

import com.piotrpabich.talktactics.entity.User;
import jakarta.persistence.PreUpdate;

public class UserEntityListeners {
    @PreUpdate
    public void beforeUpdate(User user) {
    }
}
