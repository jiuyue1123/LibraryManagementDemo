package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mapper.BookMapper;
import org.example.pojo.entity.Book;
import org.example.service.BookService;
import org.springframework.stereotype.Service;

/**
 * @author nanak
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
}
