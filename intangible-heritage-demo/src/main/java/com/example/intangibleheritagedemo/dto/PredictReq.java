package com.example.intangibleheritagedemo.dto;

public class PredictReq {
    private String text;

    // ✅ 无参构造（必须！jackson解析json要用）
    public PredictReq(){}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
