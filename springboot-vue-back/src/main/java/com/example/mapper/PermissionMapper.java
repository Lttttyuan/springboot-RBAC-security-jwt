package com.example.mapper;

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

    List<Permission> getByRoleId(@Param("rid") Integer rid);
}
