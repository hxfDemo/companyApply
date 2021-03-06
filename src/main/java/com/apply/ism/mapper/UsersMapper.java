package com.apply.ism.mapper;

import com.apply.ism.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Leon
 * @date 2020-04-20
 */
@Repository
public interface UsersMapper extends BaseMapper<Users> {
    Users getUserInfo(String username);

    List<Map<String, Object>> getUserList(String dwmc, Long sswmb, Integer shzt, Long dqcj, String sort_time, String sort_shzt);

    Map<String, Object> getUserAuditInfo(Long id);

    List<Users> getUserlists(String tcid);

   Map<String, Object>  getuserkszt(String tcid);

}