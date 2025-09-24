package org.example.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pojo.entity.BorrowRecord;
import org.example.result.Result;
import org.example.service.BorrowRecordService;
import org.springframework.web.bind.annotation.*;

/**
 * @author nanak
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/borrowRecord")
@Tag(name = "借阅记录管理")
@SaCheckLogin
public class BorrowRecordController {
    private final BorrowRecordService borrowRecordService;

    @GetMapping
    @Operation(summary = "分页查询")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        IPage<BorrowRecord> page = new Page<>(pageNum, pageSize);
        return Result.success(borrowRecordService.page(page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询借阅记录")
    public Result get(@PathVariable Long id) {
        return Result.success(borrowRecordService.getById(id));
    }
}
