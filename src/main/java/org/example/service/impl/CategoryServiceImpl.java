package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.mapper.CategoryMapper;
import org.example.pojo.entity.Category;
import org.example.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * @author nanak
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
