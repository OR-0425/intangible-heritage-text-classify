package com.example.intangibleheritagedemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("predict_record")
public class PredictRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;              // ✅ 新增：对应用户id
    private String content;
    private String label;
    private BigDecimal confidence;
    private LocalDateTime createTime;
}
