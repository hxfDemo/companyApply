package com.apply.ism.utils;

import com.apply.ism.entity.TSystem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public class TSystemTreeUtil {

	  public static Map<String,Object> mapArray = new LinkedHashMap<String, Object>();
	  public static List<TSystem> menuCommon;

	  public static List<Object> menuList(List<TSystem> menu){
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

	  public static List<?> menuChild(Long id){ //子集查找遍历
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
	        childArray.put("fraction", a.getFraction()==null?null:a.getFraction());
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
