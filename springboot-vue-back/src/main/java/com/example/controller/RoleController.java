package com.example.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Role;
import com.example.mapper.RoleMapper;
import com.example.service.IRoleService;
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
@RequestMapping("/role")
public class RoleController {
    @Resource
    private IRoleService iRoleService;

    @Resource
    private RoleMapper roleMapper;

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

        return Result.success(rolePage);
    }

    //新增
    @PostMapping("/save")
    public Result<?> save(@RequestBody Role Role){
        iRoleService.save(Role);
        return Result.success();
    }

    //编辑
    @PutMapping("/updata")
    public Result<?> updata(@RequestBody Role Role){
        iRoleService.updateById(Role);
        return Result.success();
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id){
        iRoleService.removeById(id);
        return Result.success();
    }
}
