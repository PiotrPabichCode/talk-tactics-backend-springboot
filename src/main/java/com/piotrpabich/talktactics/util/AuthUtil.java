package com.piotrpabich.talktactics.util;

import com.piotrpabich.talktactics.entity.User;
import com.piotrpabich.talktactics.exception.ForbiddenException;

import static com.piotrpabich.talktactics.util.Constants.FORBIDDEN;

public final class AuthUtil {

    public static void validateIfUserHimselfOrAdmin(final User userFromHttpRequest, final User userFromPath) {
        if (!isUserHimselfOrAdmin(userFromHttpRequest, userFromPath)) {
            throw new ForbiddenException(FORBIDDEN);
        }
    }

    public static void validateIfUserAdmin(final User userFromHttpRequest) {
        if (!userFromHttpRequest.isAdmin()) {
            throw new ForbiddenException(FORBIDDEN);
        }
    }

    public static Boolean isUserHimselfOrAdmin(final User userFromHttpRequest, final User userFromPath) {
        return userFromHttpRequest.isAdmin() || userFromHttpRequest.getId().equals(userFromPath.getId());
    }

    public static Boolean isUserAdmin(final User userFromHttpRequest) {
        return userFromHttpRequest.isAdmin();
    }
}
