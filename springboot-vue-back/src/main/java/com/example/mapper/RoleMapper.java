package com.example.mapper;

import com.example.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserRole;
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
public interface RoleMapper extends BaseMapper<Role> {
    List<UserRole> getByUserId(@Param("userid") Integer userid);

}
