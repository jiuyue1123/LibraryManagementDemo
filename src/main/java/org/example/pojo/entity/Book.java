package org.example.pojo.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constant.enums.DeleteStatusEnum;

import java.time.LocalDate;

/**
 * 书籍实体类
 *
 * @author nanak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 国际标准书号（ISBN）
     */
    @NotEmpty(message = "ISBN不能为空")
    private String isbn;

    /**
     * 书名
     */
    @NotEmpty(message = "书名不能为空")
    private String title;

    /**
     * 作者
     */
    @NotEmpty(message = "作者不能为空")
    private String author;

    /**
     * 出版社
     */
    @NotEmpty(message = "出版社不能为空")
    private String publisher;

    /**
     * 出版日期
     */
    @NotNull(message = "出版日期不能为空")
    private LocalDate publishDate;

    /**
     * 分类ID（关联图书分类表）
     */
    @NotNull(message = "分类不能为空")
    private Long categoryId;

    /**
     * 总库存数量
     */
    @NotNull(message = "总库存数量不能为空")
    private Integer totalStock;

    /**
     * 可借库存数量
     */
    @NotNull(message = "可借库存数量不能为空")
    private Integer availableStock;

    /**
     * 删除状态（枚举）
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