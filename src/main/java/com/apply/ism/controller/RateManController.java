package com.apply.ism.controller;

import com.apply.ism.common.Result;
import com.apply.ism.common.utils.TokenUtil;
import com.apply.ism.entity.Users;
import com.apply.ism.service.IUsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Log4j2
@Validated
@RequestMapping("/rate")
public class RateManController {

    @Autowired
    private IUsersService iUsersService;

    /**
     * 打分员列表
     * @param user 姓名
     * @param company 单位名称
     * @return
     */
    @PostMapping("/userList")
    public Result userList(String user,String company){
        Map<String,Object> map = new HashMap<>();
        List<Users> users = null;
        try {
            if(StringUtils.isNotBlank(user)&&StringUtils.isNotBlank(company)){
                users = iUsersService.list(new QueryWrapper<Users>().ne("data_state","Delete")
                        .eq("name",user).eq("spare1",company).eq("role","rater"));
            }else if(StringUtils.isNotBlank(user)&&StringUtils.isBlank(company)){
                users = iUsersService.list(new QueryWrapper<Users>().ne("data_state","Delete")
                        .eq("name",user).eq("role","rater"));
            }else if(StringUtils.isBlank(user)&&StringUtils.isNotBlank(company)){
                users = iUsersService.list(new QueryWrapper<Users>().ne("data_state","Delete")
                        .eq("spare1",company).eq("role","rater").eq("role","rater"));
            }else{
                users = iUsersService.list(new QueryWrapper<Users>().ne("data_state","Delete").eq("role","rater"));
            }
            map.put("lists",users);
            return Result.ok(map);
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }

    /**
     * 新增打分员
     * @param name
     * @param username
     * @param phone
     * @param job
     * @param company
     * @return
     */
    @PostMapping("/insertOne")
    public Result insertOne(@NotBlank(message = "姓名[name]不能为空") String name,@NotBlank(message = "账号[username]不能为空") String username,
                            String phone, String job, String company){
        try {
            Users usr = iUsersService.getUserInfo(username);
            if (usr!=null){
                return Result.fail(102,"该账号已注册！");
            }
            Users users = new Users();
            users.setName(name);
            users.setUsername(username);
            if(StringUtils.isNotBlank(phone)&&!"undefined".equals(phone)&&!"null".equals(phone)){
                if (iUsersService.getOne(new QueryWrapper<Users>().eq("phone",phone.trim()))!=null){
                    return Result.fail(102,"该手机号已使用！");
                };
                users.setPhone(phone);
            }
            if(StringUtils.isNotBlank(job)&&!"undefined".equals(job)&&!"null".equals(job)){
                users.setRemarks(job);
            }
            if(StringUtils.isNotBlank(company)&&!"undefined".equals(company)&&!"null".equals(company)){
                users.setSpare1(company);
            }
            users.setRole("rater");
            users.setUserstate(1);
            users.setCreateTime(new Date());
            users.setCreateId(TokenUtil.getUserId());
            users.setPassword(DigestUtils.md5Hex("123456"+ TokenUtil.SECRET+username));
            boolean save = iUsersService.save(users);
            if (!save){
                return Result.error(902,"保存失败");
            }
            return Result.ok("保存成功");
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }

    /**
     * 修改打分员
     * @param id
     * @param name 姓名
     * @param username 账号
     * @param phone
     * @param job 职务
     * @param company 单位
     * @return
     */
    @PostMapping("/updateOne")
    public Result updateOne(@NotNull(message = "打分员[id]不能为空")Long id,@NotBlank(message = "姓名[name]不能为空")String name,
                            @NotBlank(message = "账号[username]不能为空")String username,String phone,String job,String company){
        try {
            Users usr = iUsersService.getUserInfo(username);
            if (usr!=null){
                return Result.fail(102,"该账号已注册！");
            }
            Users users = new Users();
            users.setId(id);
            users.setName(name);
            users.setUsername(username);
            if(StringUtils.isNotBlank(phone)&&!"undefined".equals(phone)&&!"null".equals(phone)){
                users.setPhone(phone);
            }
            if(StringUtils.isNotBlank(job)&&!"undefined".equals(job)&&!"null".equals(job)){
                users.setRemarks(job);
            }
            if(StringUtils.isNotBlank(company)&&!"undefined".equals(company)&&!"null".equals(company)){
                users.setSpare1(company);
            }
            users.setUpdateTime(new Date());
            users.setUpdateId(TokenUtil.getUserId());
            boolean update = iUsersService.updateById(users);
            if(!update){
                return Result.error(902,"编辑失败");
            }
            return Result.ok("编辑成功");
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }

    /**
     * 删除打分员
     * @param id
     * @return
     */
    @PostMapping("/deleteOne")
    public Result deleteOne(@NotNull(message = "打分员[id]不能为空")Long id){
        try {
            Users users = new Users();
            users.setId(id);
            users.setDataState("Delete");
            users.setUpdateTime(new Date());
            users.setUpdateId(TokenUtil.getUserId());
            boolean delete = iUsersService.updateById(users);
            if(!delete){
                return Result.error(902,"删除失败");
            }
            return Result.ok("删除成功");
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }

    /**
     * 修改账号状态
     * @param id
     * @param usestate
     * @return
     */
    @PostMapping("/updateState")
    public Result updateState(@NotNull(message = "打分员[id]不能为空")Long id,@NotBlank(message = "账号状态[usestate]不能为空")String usestate){
        try {
            Users users = new Users();
            users.setId(id);
            users.setDataState(usestate);
            users.setUpdateTime(new Date());
            users.setUpdateId(TokenUtil.getUserId());
            boolean state = iUsersService.updateById(users);
            if(!state){
                return Result.error(902,"修改账号状态失败");
            }
            return Result.ok("修改账号状态成功");
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }

    /**
     * 修改打分员密码
     * @param id
     * @param pwd
     * @return
     */
    @PostMapping("/updatePwd")
    public Result updatePwd(@NotNull(message = "打分员[id]不能为空")Long id, @NotBlank(message = "密码[pwd]不能为空")String pwd){
        try {
            Users byId = iUsersService.getById(id);
            Users users = new Users();
            users.setId(id);
            users.setPassword(DigestUtils.md5Hex(pwd+ TokenUtil.SECRET+byId.getUsername()));
            users.setUpdateTime(new Date());
            users.setUpdateId(TokenUtil.getUserId());
            boolean state = iUsersService.updateById(users);
            if(!state){
                return Result.error(902,"修改账号密码失败");
            }
            return Result.ok();
        }catch (Exception e){
            return Result.error(901,e.getMessage());
        }
    }
}
