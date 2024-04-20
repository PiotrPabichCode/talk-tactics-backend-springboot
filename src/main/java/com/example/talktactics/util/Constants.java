package com.example.talktactics.util;

public class Constants {
    public static final int BEGINNER_POINTS = 5;
    public static final int INTERMEDIATE_POINTS = 10;
    public static final int ADVANCED_POINTS = 15;
    public static final int BEGINNER_COMPLETED_POINTS = 100;
    public static final int INTERMEDIATE_COMPLETED_POINTS = 200;
    public static final int ADVANCED_COMPLETED_POINTS = 300;

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    public static final String COURSE_NOT_FOUND_EXCEPTION = "Course not found";
    public static final String COURSE_ITEM_NOT_FOUND_EXCEPTION = "Course item not found";
    public static final String USER_NOT_FOUND_EXCEPTION = "User not found";
    public static final String USER_COURSE_NOT_FOUND_EXCEPTION = "User course not found";
    public static final String USER_COURSE_EXISTS_EXCEPTION = "User course exists";
    public static final String USER_COURSE_ITEM_NOT_FOUND_EXCEPTION = "User course item not found";
    public static final String NOT_ENOUGH_AUTHORITIES_EXCEPTION = "Not enough authorities";
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
