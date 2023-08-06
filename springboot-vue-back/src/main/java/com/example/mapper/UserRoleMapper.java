package com.example.mapper;

import com.example.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-07-29
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 设置user对应的角色id
     * @param uid
     */
    void insertUidAndRid(@Param("uid")Integer uid,@Param("rid")Integer rid);
}
