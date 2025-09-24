package org.example.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.exception.PasswordErrorException;
import org.example.exception.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.pojo.dto.LoginDTO;
import org.example.pojo.entity.User;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author nanak
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;

    @Override
    public String login(LoginDTO dto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", dto.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UserNotFoundException();
        }
        if (!SecureUtil.md5(user.getPassword()).equals(dto.getPassword())) {
            throw new PasswordErrorException();
        }
        StpUtil.login(user.getId());
        return StpUtil.getTokenInfo().getTokenValue();
    }
}
