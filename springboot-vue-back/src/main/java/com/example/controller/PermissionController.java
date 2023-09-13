package com.example.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.utils.Result;
import com.example.entity.Permission;
import com.example.mapper.PermissionMapper;
import com.example.service.IPermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-07-29
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Resource
    private IPermissionService iPermissionService;

    @Resource
    private PermissionMapper permissionMapper;

    //分页模糊查询
    @GetMapping("/findPage")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {

        LambdaQueryWrapper<Permission> wrapper = Wrappers.<Permission>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Permission::getPermissionName, search);
        }

        Page<Permission> permissionPage = permissionMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        return Result.success(permissionPage);
    }

    //新增
    @PostMapping("/save")
    public Result<?> save(@RequestBody Permission Permission) {
        iPermissionService.save(Permission);
        return Result.success();
    }

    //查询所有权限
    @GetMapping("/all")
    public Result<?> all() {
        List<Permission> permissions = permissionMapper.selectList(null);
        return Result.success(permissions);
    }


    //编辑
    @PutMapping("/updata")
    public Result<?> updata(@RequestBody Permission Permission) {
        iPermissionService.updateById(Permission);
        return Result.success();
    }

    //删除
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        iPermissionService.removeById(id);
        return Result.success();
    }

}
