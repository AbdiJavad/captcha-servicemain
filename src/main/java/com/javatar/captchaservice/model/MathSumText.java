package com.javatar.captchaservice.model;

import nl.captcha.text.producer.TextProducer;


public class MathSumText implements TextProducer {
    private final String text;
    private final int answer;

    public MathSumText() {
        int i = (int) (Math.random() * 30);
        int j = 0;
        if (i > 10)
            j = (int) (Math.random() * 10);
        else
            j = (int) (Math.random() * 100);
        if (i > j && ((int) (Math.random() * 2) == 1)) {
            this.answer = i - j;
            this.text = i + " - " + j + " =";
        } else {
            this.answer = i + j;
            this.text = i + " + " + j + " =";
        }
    }


    @Override
    public String getText() {
        return text;
    }

    public int getAnswer() {
        return answer;
    }

}
