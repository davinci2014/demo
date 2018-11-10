package com.example.demo.web.rest.util;

import com.example.demo.web.rest.vm.ManagedUserVM;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

public class VerificationUtil {
    public static boolean checkPasswordLength(String password) {
        return StringUtils.isNotBlank(password) &&
                password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
