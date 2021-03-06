package com.apply.ism.service;

import com.apply.ism.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author WangShuaiKai
 * @since 2020-04-19
 */
public interface IUsersService extends IService<Users> {
    Users getUserInfo(String username);

    List<Map<String, Object>> getUserList(String dwmc, Long sswmb, Integer shzt, Long dqcj, String sort_time, String sort_shzt);

    Map<String, Object> getUserAuditInfo(Long id);

    String getRole(Long userId);

    List<Users> getUserlists(String tcid);
    Map<String, Object> getuserkszt(String tcid);

}
