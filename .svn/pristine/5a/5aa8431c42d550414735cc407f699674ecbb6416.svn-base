package com.apply.ism.service.impl;

import com.apply.ism.common.Result;
import com.apply.ism.common.utils.TokenUtil;
import com.apply.ism.entity.FileMessage;
import com.apply.ism.entity.MaterialInfo;
import com.apply.ism.entity.TSystem;
import com.apply.ism.entity.Users;
import com.apply.ism.mapper.TSystemMapper;
import com.apply.ism.service.IFileMessageService;
import com.apply.ism.service.IUsersService;
import com.apply.ism.service.MaterialInfoService;
import com.apply.ism.service.TSystemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class TSystemServiceImpl extends ServiceImpl<TSystemMapper, TSystem> implements TSystemService {

    @Autowired
    private IFileMessageService iFileMessageService;

    @Autowired
    MaterialInfoService materialInfoService;

    @Autowired
    private TSystemMapper tSystemMapper;
    @Override
    public int insert(TSystem tSystem) {
        return tSystemMapper.insert(tSystem);
    }

    @Override
    public TSystem findByProject(String projectName,String nd) {
        return tSystemMapper.findByProject(projectName,nd);
    }

    @Override
    public List<TSystem> selectAll(String nd) {
        return tSystemMapper.selectAll(nd);
    }

    @Override
    public List<TSystem> findFirstLevel(String nd) {
        return tSystemMapper.findFirstLevel(nd);
    }

    @Override
    public List<TSystem> findSecondLevel(String nd) {
        return tSystemMapper.findSecondLevel(nd);
    }

    @Override
    public List<TSystem> findThreeLevel(String nd) {
        return tSystemMapper.findThreeLevel(nd);
    }

    @Override
    public int deleteByNd(String nd) {
        return tSystemMapper.deleteByNd(nd);
    }

    @Override
    public int excelInsert(Map<String, List<TSystem>> listMap,String nd) {
        //查询该年份是否存在测评体系，如果存在则删除
        List<TSystem> tSystems = tSystemMapper.selectAll(nd);
        if(tSystems.size()>0 || !tSystems.isEmpty()){
            int i = tSystemMapper.deleteByNd(nd);
            if(i<=0){
                return 0;
            }
        }
        //把excel中的数据存放的数据库
        List<TSystem> objs = listMap.get("objs");
        objs.get(0).setCreateTime(new Date());
        objs.get(0).setNd(nd);
        objs.get(0).setGrade("1");
        int row = tSystemMapper.insert(objs.get(0));
        if (row<=0){
            return 0;
        }
        List<TSystem> project = listMap.get("project");
        for (TSystem sys:project) {
            sys.setCreateTime(new Date());
            sys.setParentId(objs.get(0).getId());
            sys.setNd(nd);
            sys.setGrade("2");
            int insert = tSystemMapper.insert(sys);
            if (insert<=0){
                return 0;
            }
        }
        List<TSystem> items = listMap.get("item");
        for (TSystem item:items) {
            TSystem byProject = tSystemMapper.findByProject(item.getProjectName(),nd);
            item.setCreateTime(new Date());
            item.setParentId(byProject.getId());
            item.setNd(nd);
            item.setGrade("3");
            int insert = tSystemMapper.insert(item);
            if (insert<=0){
                return 0;
            }
        }
        return 1;
    }

    public Map<String,Object> mapArray = new LinkedHashMap<String, Object>();
    public List<TSystem> menuCommon;

    /**
     * 获取测评体系树
     * @param menu
     * @return
     */
    @Override
    public List<Object> menuList(List<TSystem> menu) {
        List<Object> list = new ArrayList<>();
        menuCommon = menu;
        for (TSystem x : menu) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            mapArr.put("id", x.getId());
            mapArr.put("sys_name",x.getSysName());
            mapArr.put("category", x.getCategory());
            mapArr.put("project_name", x.getProjectName()==null?null:x.getProjectName());
            mapArr.put("standard", x.getStandard()==null?null:x.getStandard());
            mapArr.put("contents", x.getContents()==null?null:x.getContents());
            mapArr.put("fraction", x.getFraction()==null?null:x.getFraction());
            if("1".equals(x.getGrade())){
                mapArr.put("remarks",x.getCategory());
            }else if("2".equals(x.getGrade())){
                mapArr.put("remarks",x.getProjectName());
            }else if("3".equals(x.getGrade())){
                mapArr.put("remarks",x.getStandard());
            }
            mapArr.put("child", menuChild(x.getId()));  //去子集查找遍历

            list.add(mapArr);
        }
        return list;
    }

    /**
     * 获取测评项目及测评指标信息
     * @param id
     * @return
     */
    public List<?> menuChild(Long id){ //子集查找遍历
        List<Object> lists = new ArrayList<Object>();
        for(TSystem a:menuCommon){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if (null == a.getParentId()){
                continue;
            }
            if(a.getParentId().equals(id)){
                childArray.put("id", a.getId());
                childArray.put("sys_name",a.getSysName());
                childArray.put("category", a.getCategory());
                childArray.put("project_name", a.getProjectName()==null ? null :a.getProjectName());
                childArray.put("standard", a.getStandard()==null ? null :a.getStandard());
                childArray.put("contents", a.getContents()==null?null:a.getContents());
                childArray.put("score_standard", a.getScoreStandard()==null?null:a.getScoreStandard());
                childArray.put("fraction", a.getFraction()==null?null:a.getFraction());
                MaterialInfo materialInfo = materialInfoService.getOne(new QueryWrapper<MaterialInfo>().select("title,user_id,company_id,evaluation_id,file_id,auditstate").eq("evaluation_id", a.getId()).eq("data_state", "Normal").eq("user_id",TokenUtil.getUserId()));
                if (materialInfo!=null){
                    FileMessage file = iFileMessageService.getById(materialInfo.getFileId());
                    childArray.put("title", materialInfo.getTitle()!=null?materialInfo.getTitle():"");
                    childArray.put("shzt", materialInfo.getAuditstate()!=null?materialInfo.getAuditstate():0);
                    childArray.put("fileName", file!=null&&file.getFileName()!=null?file.getFileName():"");
                    childArray.put("filePath", file!=null&&file.getFilePath()!=null?file.getFilePath():"");
                }else {
                    childArray.put("title", "");
                    childArray.put("shzt", 0);
                    childArray.put("fileName", "");
                    childArray.put("filePath", "");
                }
                if("1".equals(a.getGrade())){
                    childArray.put("remarks",a.getCategory());
                }else if("2".equals(a.getGrade())){
                    childArray.put("remarks",a.getProjectName());
                }else if("3".equals(a.getGrade())){
                    childArray.put("remarks",a.getStandard());
                }
                childArray.put("child", menuChild(a.getId()));
                lists.add(childArray);
            }else {
                continue;
            }
        }
        return lists;
    }

}
