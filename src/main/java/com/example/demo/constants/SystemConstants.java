package com.example.demo.constants;

/**
 * Application constants.
 */
public final class SystemConstants {

    // Regex for acceptable logins
    public static final String REGEX_LOGIN = "^[_.@A-Za-z0-9-]*$";
    public static final String REGEX_PHONE = "^1([34578])\\d{9}$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "zh-cn";
    
    private SystemConstants() {
    }
}
