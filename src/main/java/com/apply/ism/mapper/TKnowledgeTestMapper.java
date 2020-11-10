package com.apply.ism.mapper;

import com.apply.ism.entity.TKnowledgeTest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Leon
 * @date 2020-04-27
 */
@Repository
public interface TKnowledgeTestMapper extends BaseMapper<TKnowledgeTest> {
    List<Map<String, Object>> testLits(Map<String, Object> colmnMap);

    List<Map<String, Object>> testList(String nd, String testName, Long companyId);

    List<Map<String, Object>> getTestByName(String testName);

    Integer deleteCsData(String testName);
}
