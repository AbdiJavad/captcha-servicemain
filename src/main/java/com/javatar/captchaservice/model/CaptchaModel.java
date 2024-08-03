package com.javatar.captchaservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CaptchaModel {
    private String id;
    private byte[] data;
}
