package com.example.intangibleheritagedemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.intangibleheritagedemo.dto.HeritageDTO;
import com.example.intangibleheritagedemo.dto.PredictResult;
import com.example.intangibleheritagedemo.entity.PredictRecord;
import com.example.intangibleheritagedemo.service.HeritageService;
import com.example.intangibleheritagedemo.service.PredictRecordService;
import com.example.intangibleheritagedemo.util.JwtUtil;
import com.example.intangibleheritagedemo.util.Result;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HeritageController {

    @Autowired
    private HeritageService heritageService;

    @Autowired
    private PredictRecordService recordService;

    @PostMapping("/predict")
    public Result<PredictResult> predict(@RequestBody HeritageDTO dto, HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseToken(token);
        Long userId = Long.valueOf(claims.get("userId").toString());

        // 调用预测（返回 PredictResult，不是 Map）
        PredictResult res = heritageService.predict(dto.getText());

        // 保存记录
        PredictRecord record = new PredictRecord();
        record.setUserId(userId);
        record.setContent(dto.getText());
        record.setLabel(res.getLabel());
        record.setConfidence(BigDecimal.valueOf(res.getConfidence()));
        record.setCreateTime(LocalDateTime.now());
        recordService.save(record);

        return Result.success(res);
    }

    @GetMapping("/record/list")
    public Result<List<PredictRecord>> getRecordList(HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseToken(token);
        Long userId = Long.valueOf(claims.get("userId").toString());
        String role = claims.get("role", String.class);

        List<PredictRecord> list;
        if("admin".equals(role)){
            list = recordService.list();
        }else{
            list = recordService.lambdaQuery().eq(PredictRecord::getUserId, userId).list();
        }
        return Result.success(list);
    }

    @DeleteMapping("/record/{id}")
    public Result<Void> deleteRecord(@PathVariable Long id, HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseToken(token);
        Long userId = Long.valueOf(claims.get("userId").toString());
        String role = claims.get("role", String.class);

        PredictRecord record = recordService.getById(id);
        if(record == null){
            return Result.fail("记录不存在");
        }
        if(!"admin".equals(role) && !record.getUserId().equals(userId)){
            return Result.fail("无权删除该记录");
        }
        recordService.removeById(id);
        return Result.success();
    }

    @GetMapping("/record/stat")
    public Result<List<Map<String,Object>>> getStat(HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseToken(token);
        Long userId = Long.valueOf(claims.get("userId").toString());
        String role = claims.get("role", String.class);

        QueryWrapper<PredictRecord> wrapper = new QueryWrapper<>();
        if(!"admin".equals(role)){
            wrapper.eq("user_id", userId);
        }
        wrapper.select("label", "count(*) as count").groupBy("label");
        List<Map<String,Object>> maps = recordService.listMaps(wrapper);
        return Result.success(maps);
    }
}
