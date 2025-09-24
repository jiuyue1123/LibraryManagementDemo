package org.example.pojo.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类实体类
 *
 * @author nanak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
    @NotEmpty(message = "分类名称不能为空")
    private String name;
}
