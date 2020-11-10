package com.apply.ism.mapper;

import com.apply.ism.entity.DeclareInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Leon
 * @date 2020-04-22
 */
@Repository
public interface DeclareInfoMapper extends BaseMapper<DeclareInfo> {
    List<Map<String, Object>> declareList(String dwmc, Long sswmb, Long shzt, Long dqcj, Long sbcj, String sort_time, String sort_shzt);

    Map<String, Object> getDeclareAuditInfo(Long id);
}