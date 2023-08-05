package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2023-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("permission")
@ApiModel(value="Permission对象", description="")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer pid;

    @ApiModelProperty(value = "名称")
    private String permissionName;

    @ApiModelProperty(value = "资源路径")
    private String permissionPath;

    @ApiModelProperty(value = "备注")
    private String permissionComment;

    @ApiModelProperty(value = "图标")
    private String icon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return permissionPath.equals(that.permissionPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissionPath);
    }
}
