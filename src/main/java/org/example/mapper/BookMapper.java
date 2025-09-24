package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.pojo.entity.Book;

/**
 * @author nanak
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
