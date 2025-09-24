package org.example.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.UpdateUserDTO;
import org.example.pojo.entity.User;
import org.example.result.Result;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * @author nanak
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "用户管理")
@SaCheckLogin
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "分页查询")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        return Result.success(userService.page(page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询用户信息")
    public Result get(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Result delete(@PathVariable Long id) {
        return Result.success(userService.removeById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户")
    public Result update(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        User user = User.builder()
                .id(id)
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .status(dto.getStatus()).build();

        if (dto.getPassword() != null) {
            user.setPassword(SecureUtil.md5(dto.getPassword()));
        }

        return Result.success(userService.updateById(user));
    }

}
