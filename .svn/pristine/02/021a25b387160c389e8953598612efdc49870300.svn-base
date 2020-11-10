package com.apply.ism.service.impl;

import com.apply.ism.entity.Company;
import com.apply.ism.mapper.CompanyMapper;
import com.apply.ism.service.ICompanyService;
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
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public List<Map<String, Object>> getTontruInfo(Long id) {
        return companyMapper.getTontruInfo(id);
    }

    @Override
    public List<Map<String, Object>> getZcInfos(Long zcdw) {
        return companyMapper.getZcInfos(zcdw);
    }

    @Override
    public List<Map<String, Object>> getCompanyInfo() {
        return companyMapper.getCompanyInfo();
    }

    @Override
    public List<Map<String, Object>> getStatisticsList(String year, String company, Long djmc, Long sswmb, Long zctc) {
        return companyMapper.getStatisticsList(year,company,djmc,sswmb,zctc);
    }

    @Override
    public List<Map<String, Object>> getCompanyStatisticsList(String dwmc, Long zjdj, Long dwxz, Long sswmb, Integer zctc) {
        return companyMapper.getCompanyStatisticsList(dwmc,zjdj,dwxz,sswmb,zctc);
    }

    @Override
    public List<Map<String, Object>> getTontru(String toString) {
        return companyMapper.getTontru(toString);
    }
}
