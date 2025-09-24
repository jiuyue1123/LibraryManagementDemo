package org.example.constant.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 借阅状态枚举 借阅状态 0-在借，1-已还，2-逾期未还
 *
 * @author nanak
 */
@Getter
@AllArgsConstructor
public enum BorrowStatusEnum {
    BORROWING(0, "在借"),
    RETURNED(1, "已还"),
    OVERDUE(2, "逾期未还");

    @EnumValue
    @JsonValue
    private Integer code;
    private String message;
}
