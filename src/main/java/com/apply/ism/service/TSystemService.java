package com.apply.ism.service;

import com.apply.ism.entity.TSystem;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface TSystemService extends IService<TSystem> {
    int insert(TSystem tSystem);
    TSystem findByProject(String projectName,String nd);
    List<TSystem> selectAll(String nd);
    List<TSystem> findFirstLevel(String nd);
    List<TSystem> findSecondLevel(String nd);
    List<TSystem> findThreeLevel(String nd);
    int deleteByNd(String nd);

    @Transactional
    int excelInsert(Map<String, List<TSystem>> map,String nd);

    List<Object> menuList(List<TSystem> tSystemList);
}
