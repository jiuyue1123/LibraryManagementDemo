package org.example.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色枚举 角色，0-普通用户，1-管理员
 *
 * @author nanak
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    USER(0, "普通用户"),
    ADMIN(1, "管理员");

    @EnumValue
    @JsonValue
    private Integer code;
    private String message;
}
