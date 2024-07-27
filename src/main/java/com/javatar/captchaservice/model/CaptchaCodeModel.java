package com.javatar.captchaservice.model;

import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
public class CaptchaCodeModel {

    
    private String answer;
    private Long expireAt;

    public CaptchaCodeModel(String answer) {
        this.answer = answer;
        this.expireAt = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2);
    }
}
