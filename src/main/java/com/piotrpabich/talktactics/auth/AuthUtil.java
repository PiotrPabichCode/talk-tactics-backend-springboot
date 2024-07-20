package com.piotrpabich.talktactics.auth;

import com.piotrpabich.talktactics.user.entity.User;
import com.piotrpabich.talktactics.exception.ForbiddenException;

public final class AuthUtil {

    public static void validateIfUserHimselfOrAdmin(final User userFromHttpRequest, final User userFromPath) {
        if (!isUserHimselfOrAdmin(userFromHttpRequest, userFromPath)) {
            throw new ForbiddenException(AuthConstants.FORBIDDEN);
        }
    }

    public static void validateIfUserAdmin(final User userFromHttpRequest) {
        if (!userFromHttpRequest.isAdmin()) {
            throw new ForbiddenException(AuthConstants.FORBIDDEN);
        }
    }

    public static Boolean isUserHimselfOrAdmin(final User userFromHttpRequest, final User userFromPath) {
        return userFromHttpRequest.isAdmin() || userFromHttpRequest.getId().equals(userFromPath.getId());
    }

    public static Boolean isUserAdmin(final User userFromHttpRequest) {
        return userFromHttpRequest.isAdmin();
    }
}
