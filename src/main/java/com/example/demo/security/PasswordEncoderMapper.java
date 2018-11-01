package com.example.demo.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderMapper implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        System.out.println("输入的密码为: " + charSequence.toString());
        return charSequence.toString() + "davinci";
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        System.out.println("charSequence == " + charSequence);
        System.out.println("s == " + s);
        return s.equals(charSequence.toString() + "davinci");
    }

}
