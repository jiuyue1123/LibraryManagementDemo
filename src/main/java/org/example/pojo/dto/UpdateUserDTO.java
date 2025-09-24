package org.example.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constant.enums.UserStatusEnum;

/**
 * @author nanak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private String nickname;
    private String phone;
    private String password;
    private UserStatusEnum status;
}
