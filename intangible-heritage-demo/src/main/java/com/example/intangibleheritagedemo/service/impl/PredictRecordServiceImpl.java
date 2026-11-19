package com.example.intangibleheritagedemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.intangibleheritagedemo.entity.PredictRecord;
import com.example.intangibleheritagedemo.mapper.PredictRecordMapper;
import com.example.intangibleheritagedemo.service.PredictRecordService;
import org.springframework.stereotype.Service;

@Service
public class PredictRecordServiceImpl extends ServiceImpl<PredictRecordMapper, PredictRecord>
        implements PredictRecordService {
}
