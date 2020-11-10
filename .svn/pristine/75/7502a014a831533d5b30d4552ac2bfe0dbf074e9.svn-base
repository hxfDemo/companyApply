package com.apply.ism.service.impl;

import com.apply.ism.entity.Users;
import com.apply.ism.mapper.UsersMapper;
import com.apply.ism.service.IUsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 注释
 *
 * @author WangShuaiKai
 * @date 2020/4/19 11:49
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users getUserInfo(String username) {
        return usersMapper.getUserInfo(username);
    }

    @Override
    public List<Map<String, Object>> getUserList(String dwmc, Long sswmb, Integer shzt, Long dqcj, String sort_time, String sort_shzt) {
        return usersMapper.getUserList(dwmc,sswmb,shzt,dqcj,sort_time,sort_shzt);
    }

    @Override
    public Map<String, Object> getUserAuditInfo(Long id) {
        return usersMapper.getUserAuditInfo(id);
    }

    @Override
    public String getRole(Long userId) {
        Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq("id",userId));
        if (users == null){
            return null;
        }
        return users.getRole();
    }

    @Override
    public List<Users> getUserlists(String tcid) {
        return usersMapper.getUserlists(tcid);
    }

    @Override
    public Map<String,Object> getuserkszt(String tcid) {
        return usersMapper.getuserkszt(tcid);
    }
}
