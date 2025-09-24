package org.example.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pojo.entity.Book;
import org.example.result.Result;
import org.example.service.BookService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author nanak
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
@Tag(name = "图书管理")
@SaCheckLogin
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "分页查询")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        IPage<Book> page = new Page<>(pageNum, pageSize);
        return Result.success(bookService.page(page));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除")
    public Result delete(@PathVariable Long id) {
        return Result.success(bookService.removeById(id));
    }

    @DeleteMapping
    @Operation(summary = "批量删除")
    public Result batchDelete(@RequestBody List<Long> ids) {
        bookService.removeByIds(ids);
        return Result.success();
    }

    @PostMapping
    @Operation(summary = "添加")
    public Result add(@Validated @RequestBody Book book) {
        bookService.save(book);
        return Result.success();
    }
}
