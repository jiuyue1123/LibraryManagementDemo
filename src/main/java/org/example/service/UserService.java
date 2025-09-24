package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.pojo.dto.LoginDTO;
import org.example.pojo.entity.User;

/**
 * @author nanak
 */
public interface UserService extends IService<User> {
    String login(LoginDTO dto);
}
