package com.apply.ism.service.impl;

import com.apply.ism.entity.DeclareInfo;
import com.apply.ism.mapper.DeclareInfoMapper;
import com.apply.ism.service.IDeclareInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class DeclareInfoServiceImpl extends ServiceImpl<DeclareInfoMapper, DeclareInfo> implements IDeclareInfoService {

    @Autowired
    private DeclareInfoMapper declareInfoMapper;

    @Override
    public List<Map<String, Object>> declareList(String dwmc, Long sswmb, Long shzt, Long dqcj, Long sbcj, String sort_time, String sort_shzt) {
        return declareInfoMapper.declareList(dwmc,sswmb,shzt,dqcj,sbcj,sort_time,sort_shzt);
    }

    @Override
    public Map<String, Object> getDeclareAuditInfo(Long id) {
        return declareInfoMapper.getDeclareAuditInfo(id);
    }
}
