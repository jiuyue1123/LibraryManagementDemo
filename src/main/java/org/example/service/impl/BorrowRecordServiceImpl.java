package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mapper.BorrowRecordMapper;
import org.example.pojo.entity.BorrowRecord;
import org.example.service.BorrowRecordService;
import org.springframework.stereotype.Service;

/**
 * @author nanak
 */
@Service
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {
}
