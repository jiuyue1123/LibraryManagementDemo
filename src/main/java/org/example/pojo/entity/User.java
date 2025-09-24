package org.example.pojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constant.enums.DeleteStatusEnum;
import org.example.constant.enums.RoleEnum;
import org.example.constant.enums.UserStatusEnum;

import java.time.LocalDate;

/**
 * 用户实体类
 *
 * @author nanak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户名（如：lms_12345）
     */
    private String username;

    /**
     * 密码（通常存储加密后的密文）
     */
    @NotNull(message = "密码不能为空")
    @JsonIgnore
    private String password;

    /**
     * 昵称
     */
    @NotNull(message = "昵称不能为空")
    private String nickname;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String phone;

    /**
     * 用户状态（启用/禁用等）
     */
    private UserStatusEnum status;

    /**
     * 用户角色（管理员/普通用户等）
     */
    @NotNull(message = "用户角色不能为空")
    private RoleEnum role;

    /**
     * 删除状态（未删除/已删除）
     */
    private DeleteStatusEnum isDelete;

    /**
     * 创建时间
     */
    private LocalDate createTime;

    /**
     * 更新时间
     */
    private LocalDate updateTime;
}