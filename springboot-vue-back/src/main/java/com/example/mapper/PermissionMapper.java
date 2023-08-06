package com.example.mapper;
import java.util.Collection;

import com.example.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-07-29
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据rid查询出对应的资源信息
     * @param rid
     * @return
     */
    List<Permission> getByRoleId(@Param("rid") Integer rid);

    /**
     * 根据rid删除role_permission表中对应的资源pid
     * @param rid
     */
    void deletePermissionByRoleId(@Param("rid") Integer rid);

    void insertRoleAndPermission(@Param("rid")Integer rid,@Param("pid")Integer pid);
}
