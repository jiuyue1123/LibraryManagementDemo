package org.example.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pojo.entity.Category;
import org.example.result.Result;
import org.example.service.CategoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author nanak
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
@Tag(name = "类别管理")
@SaCheckLogin
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "查询列表")
    public Result list() {
        List<Category> list = categoryService.list();
        return Result.success(list);
    }

    @PostMapping
    @Operation(summary = "添加类别")
    public Result add(@Validated @RequestBody Category category) {
        categoryService.save(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除类别")
    public Result delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "批量删除类别")
    public Result batchDelete(@RequestBody List<Long> ids) {
        categoryService.removeByIds(ids);
        return Result.success();
    }
}
