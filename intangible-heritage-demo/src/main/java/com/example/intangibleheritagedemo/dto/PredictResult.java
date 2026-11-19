package com.example.intangibleheritagedemo.dto;

public class PredictResult {
    private String label;
    private Double confidence;

    // 无参构造（必须，否则json转对象失败）
    public PredictResult(){}

    // getter & setter
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public Double getConfidence() {
        return confidence;
    }
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}
