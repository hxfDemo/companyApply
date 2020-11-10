package com.apply.ism.service.impl;

import com.apply.ism.entity.TKnowledge;
import com.apply.ism.mapper.TKnowledgeMapper;
import com.apply.ism.service.TKnowledgeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class TKnowledgeServiceImpl extends ServiceImpl<TKnowledgeMapper, TKnowledge> implements TKnowledgeService {
}
