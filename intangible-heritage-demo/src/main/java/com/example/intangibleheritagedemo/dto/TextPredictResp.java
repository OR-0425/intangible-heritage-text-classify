// TextPredictResp.java
package com.example.intangibleheritagedemo.dto;

public class TextPredictResp {
    private String label;
    private Double score;

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }
}
