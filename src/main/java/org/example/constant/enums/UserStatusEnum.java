package org.example.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举类 状态，0-正常，1-禁用
 *
 * @author nanak
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    NORMAL(0, "正常"),
    DISABLED(1, "禁用"),;

    @EnumValue
    @JsonValue
    private Integer code;
    private String message;
}
