package org.example.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.constant.enums.RoleEnum;
import org.example.pojo.dto.LoginDTO;
import org.example.pojo.dto.RegisterDTO;
import org.example.pojo.entity.User;
import org.example.pojo.vo.LoginVO;
import org.example.result.Result;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nanak
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "用户认证管理")
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result login(LoginDTO dto) {
        String token = userService.login(dto);
        LoginVO vo = LoginVO.builder()
                .token(token).build();
        return Result.success(vo);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result register(RegisterDTO dto) {
        // 生成用户名
        String username = "lms_" + RandomUtil.randomNumbers(5);
        // 查询用户名是否已存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User one = userService.getOne(wrapper);
        while (one != null) {
            username = "lms_" + RandomUtil.randomNumbers(5);
            one = userService.getOne(wrapper);
        }
        User user = User.builder()
                .username(username)
                .phone(dto.getPhone())
                .role(RoleEnum.USER)
                .password(SecureUtil.md5(dto.getPassword())).build();
        userService.save(user);
        return Result.success();
    }
}
