package com.example.common.security.handler;

import cn.hutool.json.JSONUtil;
import com.example.common.security.UserSecurity;
import com.example.entity.Permission;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.mapper.PermissionMapper;
import com.example.mapper.RoleMapper;
import com.example.utils.JwtUtils;
import com.example.utils.RedisUtils;
import com.example.utils.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    PermissionMapper permissionMapper;
    @Value("${my.token.expire}")
    private Integer expire; //7天，s为单位

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        //从认证信息中获取登录用户信息
        UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
        User user = userSecurity.getUser();
        //序列化
        String userInfo = objectMapper.writeValueAsString(user);
        //获取权限信息
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) userSecurity.getAuthorities();
        List<String> userPermissions = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());

        List<Permission> allPermissionsByUserId = permissionMapper.getAllPermissionsByUserId(user.getId());

        //设置当前用户的资源信息
        user.setPermissions(allPermissionsByUserId);

        // 生成JWT，并放置到请求头中
        String jwt = jwtUtils.generateToken(user.getUsername(), userInfo, userPermissions);
        httpServletResponse.setHeader(jwtUtils.getHeader(), jwt);

        //设置当前登录用户的token
        user.setToken(jwt);

        //将jwt放到redis中，设置过期时间和jwt过期时间一致
        redisUtils.setCacheObject("loginToken:" + jwt, objectMapper.writeValueAsString(authentication), expire, TimeUnit.SECONDS);

        //将用户数据传递到前端
        Result result = Result.success(user);
        log.info("jwt生成成功且放入redis中");
        log.info("jwt------>" + jwt);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
