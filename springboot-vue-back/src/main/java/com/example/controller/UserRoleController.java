package com.example.controller;


import com.example.utils.Result;
import com.example.entity.User;
import com.example.mapper.UserRoleMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2023-07-29
 */
@RestController
@RequestMapping("/userRole")
public class UserRoleController {

    @Resource
    private UserRoleMapper userRoleMapper;

    //修改
    @PutMapping("/updata/changeUserRole")
    Result<?> updataChangeUserRole(@RequestBody User user){
        //先删除user_role中角色对应关系，再重新添加
        userRoleMapper.deleteById(user.getId());

        String userRole = user.getRole();
        if (userRole.contains("管理员")){
            userRoleMapper.insertUidAndRid(user.getId(),1);
        }
        if (userRole.contains("普通用户")) {
            userRoleMapper.insertUidAndRid(user.getId(),2);
        }
        return Result.success();
    }
    //新增
    @PostMapping("/save/changeUserRole")
    Result<?> saveChangeUserRole(@RequestBody User user){
        String userRole = user.getRole();
        if (userRole.contains("管理员")){
            userRoleMapper.insertUidAndRid(user.getId(),1);
        }
        if (userRole.contains("普通用户")) {
            userRoleMapper.insertUidAndRid(user.getId(),2);
        }
        return Result.success();
    }
}
