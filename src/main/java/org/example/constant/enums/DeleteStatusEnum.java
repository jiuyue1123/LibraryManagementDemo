package org.example.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除状态枚举
 *
 * @author nanak
 */
@Getter
@AllArgsConstructor
public enum DeleteStatusEnum {
    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    @EnumValue
    @JsonValue
    private Integer code;
    private String message;
}
