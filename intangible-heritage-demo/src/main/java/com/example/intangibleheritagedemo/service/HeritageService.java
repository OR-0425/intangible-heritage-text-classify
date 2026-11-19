package com.example.intangibleheritagedemo.service;

import com.example.intangibleheritagedemo.dto.PredictResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class HeritageService {

    @Resource
    private RestTemplate restTemplate;

    private final String fastApiUrl = "http://127.0.0.1:8000/predict";

    /**
     * 只负责调用 FastAPI 模型，不做入库
     */
    public PredictResult predict(String text) {
        Map<String, String> reqBody = Map.of("text", text);
        Map<String, Object> result = restTemplate.postForObject(fastApiUrl, reqBody, Map.class);

        PredictResult res = new PredictResult();
        res.setLabel((String) result.get("label"));

        // 防止模型没返回 confidence 时 NPE
        Object conf = result.get("confidence");
        res.setConfidence(conf == null ? 0.0 : Double.valueOf(conf.toString()));

        return res;
    }
}
