package com.example.common.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Permission;
import com.example.entity.User;
import com.example.service.impl.PermissionServiceImpl;
import com.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//第一步：根据用户名查询出用户信息及权限信息
@Service
public class UserSecurityServiceImpl implements UserDetailsService {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PermissionServiceImpl permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = userService.getOne(queryWrapper);
        if (user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        List<Permission> permissionInfo = permissionService.getAllPermissionsByUserId(user.getId());

        List<String> permissions = permissionInfo.stream().map(Permission::getPermissionPath).collect(Collectors.toList());

        List<SimpleGrantedAuthority> userPermissions = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        UserSecurity userSecurity = new UserSecurity(user,userPermissions);

        return userSecurity;
    }
}
