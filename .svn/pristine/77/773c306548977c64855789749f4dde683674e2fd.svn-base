package com.apply.ism.mapper;

import com.apply.ism.entity.TSystem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Leon
 * @date 2020-04-22
 */
@Repository
public interface TSystemMapper extends BaseMapper<TSystem> {
    TSystem findByProject(String projectName,String nd);

    List<TSystem> selectAll(String nd);
    List<TSystem> findFirstLevel(String nd);
    List<TSystem> findSecondLevel(String nd);
    List<TSystem> findThreeLevel(String nd);

    int deleteByNd(String nd);
}
