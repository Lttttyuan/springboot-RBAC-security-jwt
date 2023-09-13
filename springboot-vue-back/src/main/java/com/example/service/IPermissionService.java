package com.example.service;

import com.example.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2023-07-29
 */
public interface IPermissionService extends IService<Permission> {

    List<Permission> getAllPermissionsByUserId(Integer id);
}
