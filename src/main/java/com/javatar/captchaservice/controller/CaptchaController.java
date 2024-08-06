package com.javatar.captchaservice.controller;

import com.javatar.captchaservice.model.CaptchaModel;
import com.javatar.captchaservice.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("captcha")
public class CaptchaController {

    
    @Autowired
    private CaptchaService captchaService;

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getCaptchaImage() {

        CaptchaModel captchaData = captchaService.generateCaptchaImage(null);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "captcha.png");
        httpHeaders.add("captchaId", captchaData.getId());
        httpHeaders.add("Access-Control-Expose-Headers", "captchaId");
        ByteArrayResource resource = new ByteArrayResource(captchaData.getData());
        MediaType mediaType = MediaType.IMAGE_PNG;
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentType(mediaType)
                .body(resource);
    }

}
