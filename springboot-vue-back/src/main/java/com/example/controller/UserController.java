package com.example.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Permission;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.mapper.PermissionMapper;
import com.example.mapper.RoleMapper;
import com.example.mapper.UserMapper;
import com.example.service.IUserService;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-06-30
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService iUserService;

    @Resource
    private UserMapper userMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    PermissionMapper permissionMapper;

    //登录
    //不能存在相同用户名
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",user.getUsername());
        queryWrapper.eq("password",user.getPassword());

        User res = userMapper.selectOne(queryWrapper);
        if (res == null) {
            return Result.error("-1", "用户名或密码错误");
        }

        //从user_role_rbac视图中通过userid查询所有对应的role_id
        Integer userId = user.getId();
        List<UserRole> roleIds = roleMapper.getByUserId(userId);

        List<Permission> permissionList = new ArrayList<>();

        for (UserRole roleId : roleIds) {
            //根据角色id查询所有的资源
            permissionList.addAll(permissionMapper.getByRoleId(roleId.getRid()));
        }

        //重写equals和hashcode方法去除不同role中相同的permissionPath
        HashSet<Permission> permissionSet = new HashSet<>(permissionList);
        user.setPermissions(permissionSet);
        user.getPermissions();

        return Result.success(res);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));

        if (res != null) {
            return Result.error("-1", "用户名重复");
        }
        if (user.getPassword() == null) {
            user.setPassword("123456");
        }
        iUserService.save(user);
        return Result.success();
    }

    //分页模糊查询
    @GetMapping("/findPage")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {

        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getUsername, search);
        }

        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);

        return Result.success(userPage);
    }

    //根据用户名查询
    @GetMapping("/find/{search}")
    public Result<?> find(@PathVariable String search) {
        /**
         * 方法三：查询多条数据库中的记录---条件查询
         * List<T> selectList(@Param("ew") Wrapper<T> queryWrapper);
         */
        //首先构造QueryWrapper来进行条件的添加
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("username", search);//相当于where id=1

        User user = userMapper.selectOne(wrapper);
        /**
         * 返回值结果
         * {"id": 1,"name": "df","age": 222}
         */
        return Result.success(user);
    }

    //根据id查询
    @GetMapping("/findId/{id}")
    public Result<?> findId(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        return Result.success(user);
    }

    //新增
    @PostMapping("/save")
    public Result<?> save(@RequestBody User user) {
        if (user.getPassword() == null) {
            user.setPassword("123456");
        }
        iUserService.save(user);
        return Result.success();
    }

    //编辑
    @PutMapping("/updata")
    public Result<?> updata(@RequestBody User user) {
        iUserService.updateById(user);
        return Result.success();
    }


    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {

        userMapper.deleteById(id);

        return Result.success();
    }


}
