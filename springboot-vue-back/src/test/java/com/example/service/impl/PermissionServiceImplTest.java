package com.example.service.impl;

import com.example.entity.Permission;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
@Slf4j
class PermissionServiceImplTest {

    @Autowired
    PermissionServiceImpl permissionService;
    @Test
    void getAllPermissionsByUserId() {
        List<Permission> allPermissionsByUserId = permissionService.getAllPermissionsByUserId(3);
        log.info(allPermissionsByUserId.toString());
        List<String> permissions = allPermissionsByUserId.stream().map(Permission::getPermissionPath).collect(Collectors.toList());
        log.info(permissions.toString());
    }
}