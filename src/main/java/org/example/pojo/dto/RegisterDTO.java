package org.example.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册DTO
 *
 * @author nanak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NotNull(message = "昵称不能为空")
    private String nickname;
    @NotNull(message = "手机号不能为空")
    private String phone;
    @NotNull(message = "密码不能为空")
    private String password;
}
