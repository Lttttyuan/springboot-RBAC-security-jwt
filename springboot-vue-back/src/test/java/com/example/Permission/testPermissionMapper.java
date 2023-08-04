package com.example.Permission;

import com.example.entity.Permission;
import com.example.entity.User;
import com.example.entity.UserRole;
import com.example.mapper.PermissionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
public class testPermissionMapper {
    @Resource
    private PermissionMapper permissionMapper;

    @Test
    public void testGetByRoleId(){
        HashSet<Permission> permissionSet = new HashSet<>();

        //根据角色id查询所有的资源
        permissionSet.addAll(permissionMapper.getByRoleId(1));
        permissionSet.addAll(permissionMapper.getByRoleId(2));

        //重写equals和hashcode方法去除不同role中相同的permissionPath
        System.out.println(permissionSet);
    }

    @Test
    public void testListAddAndListAddALL(){
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        System.out.println(list1);

        List<Integer> list2 = new ArrayList<>();
        list2.addAll(list1);
        list2.addAll(list1);
        System.out.println(list2);
    }
}
