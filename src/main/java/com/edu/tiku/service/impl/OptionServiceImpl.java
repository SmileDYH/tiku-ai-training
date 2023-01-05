package com.edu.tiku.service.impl;

import com.edu.tiku.model.entity.QuestionOption;
import com.edu.tiku.mapper.OptionMapper;
import com.edu.tiku.service.OptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 选项表 服务实现类
 * </p>
 *
 * @author dyh
 * @since 2023-01-05
 */
@Service
public class OptionServiceImpl extends ServiceImpl<OptionMapper, QuestionOption> implements OptionService {

}
