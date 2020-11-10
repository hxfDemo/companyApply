package com.apply.ism.service;

import com.apply.ism.entity.DeclareInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface IDeclareInfoService extends IService<DeclareInfo> {
    List<Map<String, Object>> declareList(String dwmc, Long sswmb, Long shzt, Long dqcj, Long sbcj, String sort_time, String sort_shzt);

    Map<String, Object> getDeclareAuditInfo(Long id);
}
