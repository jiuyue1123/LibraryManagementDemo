package org.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constant.enums.BorrowStatusEnum;

import java.time.LocalDateTime;

/**
 * 借阅记录实体类
 *
 * @author nanak
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRecord {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private BorrowStatusEnum status;
}
