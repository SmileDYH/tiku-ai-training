package com.edu.tiku.service.impl;

import com.edu.tiku.model.entity.Question;
import com.edu.tiku.mapper.QuestionMapper;
import com.edu.tiku.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 试题表 服务实现类
 * </p>
 *
 * @author dyh
 * @since 2023-01-05
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

}
