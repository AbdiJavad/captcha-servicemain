package com.javatar.captchaservice.service;

import com.javatar.captchaservice.model.CaptchaCodeModel;
import com.javatar.captchaservice.model.CaptchaModel;
import com.javatar.captchaservice.model.MathSumText;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.backgrounds.TransparentBackgroundProducer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CaptchaService {

    private static final int CAPTCHA_WIDTH = 200, CAPTCHA_HEIGHT = 50;
    private static final Map<String, CaptchaCodeModel> captchaCodeMap = new HashMap<String, CaptchaCodeModel>();

    private SecureRandom random = new SecureRandom();

    public String nextCaptchaId() {
        return new BigInteger(130, random).toString(32);
    }

    List<Color> colors = Collections.singletonList(new Color(223, 232, 232));
    List<Font> fonts = Collections.singletonList(new Font("Arial", 1, 40));
    GradiatedBackgroundProducer backgroundProducer = new GradiatedBackgroundProducer();

    public CaptchaModel generateCaptchaImage(String previousCaptchaId) {
        backgroundProducer.setFromColor(new Color(109, 239, 255));
        backgroundProducer.setToColor(new Color(206, 255, 255));
        if (previousCaptchaId != null)
            removeCaptcha(previousCaptchaId);
        MathSumText mathSumText = new MathSumText();
        Captcha captcha = new Captcha.Builder(CAPTCHA_WIDTH, CAPTCHA_HEIGHT).addText(mathSumText)
                .addBackground(new TransparentBackgroundProducer())
                .addNoise(new CurvedLineNoiseProducer(Color.white, 2.0F))
                .addNoise(new CurvedLineNoiseProducer(Color.white, 2.0F))
                .build();


        BufferedImage buf = captcha.getImage();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        String captchaPngImage = null;

        try {
            ImageIO.write(buf, "png", bao);
            bao.flush();
            byte[] imageBytes = bao.toByteArray();

            String captchaId = this.nextCaptchaId();
            addCaptcha(captchaId, mathSumText.getAnswer() + "");
            return new CaptchaModel(captchaId, imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean validateCaptcha(String captchaId, String captchaAnswer) {
        boolean result = false;
        long now = System.currentTimeMillis();
        CaptchaCodeModel captchaCodeModel = captchaCodeMap.get(captchaId);
        if (captchaCodeMap.containsKey(captchaId) && captchaCodeModel.getAnswer().equals(captchaAnswer) && now < captchaCodeModel.getExpireAt())
            result = true;
        removeCaptcha(captchaId);
        return result;
    }

    private static void addCaptcha(String captchaId, String captchaAnswer) {
        captchaCodeMap.putIfAbsent(captchaId, new CaptchaCodeModel(captchaAnswer));
    }

    private static void removeCaptcha(String captchaId) {
        captchaCodeMap.remove(captchaId);
    }
}
