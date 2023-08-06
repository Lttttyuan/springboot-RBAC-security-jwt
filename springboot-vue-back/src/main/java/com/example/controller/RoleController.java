package com.example.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Permission;
import com.example.entity.Role;
import com.example.entity.RolePermission;
import com.example.entity.User;
import com.example.mapper.PermissionMapper;
import com.example.mapper.RoleMapper;
import com.example.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2023-07-29
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private IRoleService iRoleService;

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    //分页模糊查询
    @GetMapping("/findPage")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<Role> wrapper = Wrappers.<Role>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(Role::getRoleName, search);
        }

        Page<Role> rolePage = roleMapper.selectPage(new Page<>(pageNum,pageSize),wrapper);

        //给角色设置绑定的权限id数组
        List<Role> records = rolePage.getRecords();
        for (Role record : records) {
            Integer rid = record.getRid();
            List<Integer> permissions = permissionMapper.getByRoleId(rid).stream().map(Permission::getPid).collect(Collectors.toList());
            record.setPermissions(permissions);
        }

        return Result.success(rolePage);
    }

    //新增
    @PostMapping("/save")
    public Result<?> save(@RequestBody Role role){
        iRoleService.save(role );
        return Result.success();
    }

    //编辑
    @PutMapping("/updata")
    public Result<?> updata(@RequestBody Role role){
        iRoleService.updateById(role);
        return Result.success();
    }

    //改变角色权限
    @PutMapping("/changePermission")
    public Result<?> changePermission(@RequestBody Role role){
        List<Integer> permissions = role.getPermissions();

        //先根据角色id删除所有的角色跟权限的绑定关系
        permissionMapper.deletePermissionByRoleId(role.getRid());
        //再新增新的绑定关系
        for (Integer permissionId : permissions) {
            permissionMapper.insertRoleAndPermission(role.getRid(), permissionId);
        }
        return Result.success();
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        iRoleService.removeById(id);
        return Result.success();
    }
}
