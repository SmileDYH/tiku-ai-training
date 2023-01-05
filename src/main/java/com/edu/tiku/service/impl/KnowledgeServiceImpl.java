package com.edu.tiku.service.impl;

import com.edu.tiku.model.entity.Knowledge;
import com.edu.tiku.mapper.KnowledgeMapper;
import com.edu.tiku.service.KnowledgeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 知识点表 服务实现类
 * </p>
 *
 * @author dyh
 * @since 2023-01-05
 */
@Service
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge> implements KnowledgeService {

}
