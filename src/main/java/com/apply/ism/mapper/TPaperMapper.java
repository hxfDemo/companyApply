package com.apply.ism.mapper;

import com.apply.ism.entity.TPaper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Leon
 * @date 2020-04-27
 */
@Repository
public interface TPaperMapper extends BaseMapper<TPaper> {
    List<Map<String, Object>> getPaperList(String dwmc);
    List<Map<String, Object>> testListScore(String dwmc);
}