package com.tlias.controller;

import com.tlias.pojo.Result;
import com.tlias.pojo.entity.Feedback;
import com.tlias.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 反馈管理控制器
 * 处理用户反馈信息的CRUD操作，服务于反馈管理模块
 */
@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {
    @Autowired private FeedbackService feedbackService;

    /**
     * 查询反馈列表，支持按类型和状态筛选
     * @param type 反馈类型（可选，如1-投诉、2-建议等）
     * @param status 反馈状态（可选，如0-待处理、1-已处理等）
     * @return 符合条件的反馈列表
     */
    @GetMapping public Result<List<Feedback>> list(
            @RequestParam(required = false) Short type,
            @RequestParam(required = false) Short status) {
        return Result.success(feedbackService.list(type, status));
    }

    /**
     * 新增反馈
     * @param feedback 反馈信息（从请求体获取）
     * @return 操作结果
     */
    @PostMapping public Result<Void> add(@RequestBody Feedback feedback) { feedbackService.add(feedback); return Result.success(); }

    /**
     * 根据ID查询反馈详情
     * @param id 反馈ID（从路径参数获取）
     * @return 反馈详细信息
     */
    @GetMapping("/{id}") public Result<Feedback> getById(@PathVariable Integer id) { return Result.success(feedbackService.getById(id)); }

    /**
     * 更新反馈信息（如处理状态等）
     * @param feedback 反馈信息（从请求体获取，需包含id）
     * @return 操作结果
     */
    @PutMapping public Result<Void> update(@RequestBody Feedback feedback) { feedbackService.update(feedback); return Result.success(); }

    /**
     * 根据ID删除反馈
     * @param id 反馈ID（从路径参数获取）
     * @return 操作结果
     */
    @DeleteMapping("/{id}") public Result<Void> delete(@PathVariable Integer id) { feedbackService.delete(id); return Result.success(); }
}
