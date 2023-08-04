package com.example.Role;

import com.example.entity.Role;
import com.example.entity.UserRole;
import com.example.mapper.RoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class testRoleMapper {
    @Resource
    private RoleMapper roleMapper;

    @Test
    public void testGetByUserId(){
        List<UserRole> roleList = roleMapper.getByUserId(3);

        System.out.println(roleList);
        for (UserRole userRole : roleList) {
            System.out.println(userRole);
        }

    }
}
