package com.apply.ism.service.impl;

import com.apply.ism.entity.FileMessage;
import com.apply.ism.mapper.FileMessageMapper;
import com.apply.ism.service.IFileMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class FileMessageImpl extends ServiceImpl<FileMessageMapper, FileMessage> implements IFileMessageService {

   @Autowired
   FileMessageMapper fileMessageMapper;

    @Override
    public int insert(FileMessage fileMessage) {
        return fileMessageMapper.insert(fileMessage);
    }
}
