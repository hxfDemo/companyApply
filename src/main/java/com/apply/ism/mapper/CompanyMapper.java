package com.apply.ism.mapper;

import com.apply.ism.entity.Company;
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
public interface CompanyMapper extends BaseMapper<Company> {
    List<Map<String, Object>> getTontruInfo(Long id);

    List<Map<String, Object>> getZcInfos(Long zcdw);

    List<Map<String, Object>> getCompanyInfo();

    List<Map<String, Object>> getStatisticsList(String year, String company, Long djmc, Long sswmb, Long zctc);

    List<Map<String, Object>> getCompanyStatisticsList(String dwmc, Long zjdj, Long dwxz, Long sswmb, Integer zctc);

    List<Map<String, Object>> getTontru(String toString);
}