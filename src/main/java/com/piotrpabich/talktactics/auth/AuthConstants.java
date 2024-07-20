package com.piotrpabich.talktactics.auth;

public class AuthConstants {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";
    public static final String FORBIDDEN = "User does not have enough authorities to perform this action";
    public static final String EMAIL_FORBIDDEN_VALUES_EXCEPTION = "Email has forbidden characters";
    public static final String INVALID_PASSWORD_EXCEPTION = "Invalid password";

    public static final String THE_SAME_PASSWORD_EXCEPTION = "Password and repeated password must be the same";
    public static final String DUPLICATED_PASSWORD_EXCEPTION = "New password must be different than the old one";
    public static final String ALL_FIELDS_REQUIRED = "All fields required";

    //    JWT
    public static final String JWT_INVALID_EXCEPTION = "The JWT token has expired";
    public static final String JWT_EXPIRED_EXCEPTION = "The JWT token has expired";
    public static final String JWT_UNSUPPORTED_EXCEPTION = "The JWT token is not supported";
    public static final String JWT_MALFORMED_EXCEPTION = "The JWT token is malformed";
    public static final String JWT_SIGNATURE_EXCEPTION = "The JWT token has an invalid signature";
    public static final String JWT_ILLEGAL_ARGUMENT_EXCEPTION = "Invalid argument passed";
    public static final String USERNAME_NOT_FOUND_EXCEPTION = "Username not found";

}
