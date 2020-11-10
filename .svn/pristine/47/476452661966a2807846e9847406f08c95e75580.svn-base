package com.apply.ism.controller;

import com.apply.ism.common.Result;
import com.apply.ism.common.utils.TokenUtil;
import com.apply.ism.entity.TKnowledge;
import com.apply.ism.mapper.TKnowledgeMapper;
import com.apply.ism.service.TKnowledgeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.*;

@RequiredArgsConstructor
@RestController
@Log4j2
@Validated
@RequestMapping("/knowledge")
public class TKnowledgeController {
    @Autowired
    TKnowledgeMapper tKnowledgeMapper;

    @Autowired
    TKnowledgeService tKnowledgeService;

    @Value("${api.base.upload.path}")
    private String filePath;

    @Value("${spring.resources.static-locations}")
    private String downloadPath;

    private String Catalog = "/officefile/";//文件夹目录
    /**
     * 上传学习资料
     * @param request
     * @param file
     * @param title
     * @return
     */
    @PostMapping("/uploadFile")
    public Result uploadFile(HttpServletRequest request, MultipartFile[] file, String title){
        try {
            if(file.length == 0){
                return Result.error(903,"上传文件不能为空");
            }
            File dir = new File(filePath+Catalog);
            if(!dir.exists()){
                dir.mkdir();
            }
            for(int i=0;i<file.length;i++){
                if( null != file[i] ){
                    try {
                        String ext = file[0].getOriginalFilename().substring(file[0].getOriginalFilename().indexOf("."));
                        //文件存放的路径
//                String path = Catalog+file[0].getOriginalFilename();
                        String path = Catalog+System.currentTimeMillis()+ext;
                        //服务器端保存的文件对象
                        File serverFile = new File(filePath+path);
                        //将上传的文件写入的服务器端文件内
                        file[i].transferTo(serverFile);
                        TKnowledge tKnowledge = new TKnowledge();
                        tKnowledge.setCreateTime(new Date());
                        tKnowledge.setCreateId(TokenUtil.getUserId());
                        tKnowledge.setTitle(title);
                        tKnowledge.setFileName(file[i].getOriginalFilename());
                        tKnowledge.setFilePath(path);
                        boolean save = tKnowledgeService.save(tKnowledge);
                        if (!save){
                            return Result.error(902,"上传学习资料失败");
                        }
                    } catch (IOException e) {
                        return Result.error(901,e.getMessage());
                    }
                }
            }
            return Result.ok("上传成功");
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }

    /**
     * 学习资料列表
     * @param title
     * @return
     */
    @PostMapping("/knowledgeList")
    public Result knowledgeList(String title){
        try {
            Map<String,Object> map = new HashMap<>();
            List<TKnowledge> list=null;
            if (StringUtils.isNotBlank(title)){
                list = tKnowledgeService.list(new QueryWrapper<TKnowledge>().like("title",title).eq("data_state","Normal"));
            }else {
                list = tKnowledgeService.list(new QueryWrapper<TKnowledge>().eq("data_state","Normal"));
            }

            map.put("list",list);
            return Result.ok(map);
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }

    /**
     * 删除学习资料
     * @param id
     * @return
     */
    @PostMapping("/deleteOne")
    public Result deleteOne(@NotNull(message = "学习资料[id]不能为空") Long id){
        try {
            TKnowledge tKnowledge = new TKnowledge();
            tKnowledge.setId(id);
            tKnowledge.setDataState("Delete");
            boolean delete = tKnowledgeService.updateById(tKnowledge);
            if(!delete){
                return Result.error(902,"删除失败");
            }
            return Result.ok();
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }

    /**
     * 下载学习资料
     * @param src
     * @return
     */
    //@PostMapping("/downloadFile")暂不使用
    public ResponseEntity downloadFile(@NotBlank(message = "下载文件的目标路径[src]不能为空") String src) {
        org.springframework.core.io.Resource resource = null;

            src = downloadPath+src;
            String fileName = UUID.randomUUID()+src.substring(src.lastIndexOf("."));
            try {
                resource = new UrlResource(src);
            }catch (Exception e){
                e.printStackTrace();
            }
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+fileName)
                    .body(resource);
    }
}
